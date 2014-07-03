package option;

import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import lombok.Synchronized;
import net.sourceforge.peers.Config;
import net.sourceforge.peers.FileLogger;
import net.sourceforge.peers.Logger;
import net.sourceforge.peers.javaxsound.JavaxSoundManager;
import net.sourceforge.peers.media.FileReader;
import net.sourceforge.peers.media.MediaManager;
import net.sourceforge.peers.sip.core.useragent.MidDialogRequestManager;
import net.sourceforge.peers.sip.core.useragent.SipListener;
import net.sourceforge.peers.sip.core.useragent.UserAgent;
import net.sourceforge.peers.sip.syntaxencoding.SipUriSyntaxException;
import net.sourceforge.peers.sip.transactionuser.Dialog;
import net.sourceforge.peers.sip.transactionuser.DialogManager;
import net.sourceforge.peers.sip.transport.SipRequest;
import net.sourceforge.peers.sip.transport.SipResponse;

import com.musicg.wave.Wave;
import com.musicg.wave.WaveFileManager;
import com.musicg.wave.WaveHeader;

public class Phone {

	private UserAgent ua;
	private boolean registerSuccess;
	private Config config;

	// Latches to make this synchronous
	private CountDownLatch registerLatch;
	private CountDownLatch ringLatch;

	private PhoneListener listener;
	private Phone phone;

	// private Dialog dialog;
	private SipRequest activeCallReq;

	private Logger logger;

	public Phone(Config config, PhoneListener listener) {
		this.config = config;
		this.listener = listener;
		this.phone = this;
		this.logger = new FileLogger(null);
		try {
			JavaxSoundManager javaxSoundManager = new JavaxSoundManager(false,
					logger, null);
			ua = new UserAgent(new PhoneSipListener(), config, logger,
					javaxSoundManager);
		} catch (SocketException e) {
			rethrow("failed to create phone", e);
		}
	}

	@Synchronized
	public void register() {
		registerSuccess = false;
		registerLatch = new CountDownLatch(1);

		boolean completed = false;

		try {
			ua.register();
			// ua.getUac().register();
			completed = registerLatch.await(5, TimeUnit.SECONDS);
		} catch (Exception e) {
			rethrow("register fail", e);
		}

		if (completed) {
			if (!registerSuccess) {
				rethrow("register fail");
			}
		} else {
			rethrow("register timeout");
		}
	}

	@Synchronized
	public void unregister() {
		try {
			ua.unregister();
			// ua.getUac().unregister();
		} catch (SipUriSyntaxException e) {
			rethrow("unregister fail", e);
		}
	}

	@Synchronized
	public void reset() {
		try {
			// unregister();
			ua.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		registerLatch = null;
		ringLatch = null;
		// dialog = null;
		activeCallReq = null;

		try {
			JavaxSoundManager javaxSoundManager = new JavaxSoundManager(false,
					logger, null);
			ua = new UserAgent(new PhoneSipListener(), config, logger,
					javaxSoundManager);
			register();
		} catch (Exception e) {
			throw new PhoneException("reset fail", e);
		}
	}

	@Synchronized
	public void dial(String extension) {
		String sipAddr = "sip:" + extension + "@" + config.getDomain();
		dialSip(sipAddr);
	}

	@Synchronized
	public void dialSip(String sipAddr) {
		if (activeCallReq != null)
			throw new PhoneException("busy with another call");

		if (!sipAddr.startsWith("sip:")) {
			sipAddr = "sip:" + sipAddr;
		}

		String callId = "call-" + config.getUserPart();

		try {
			ringLatch = new CountDownLatch(1);
			activeCallReq = ua.invite(sipAddr, callId);
			System.out.println("sending invite");
		} catch (SipUriSyntaxException e) {
			e.printStackTrace();
			throw new PhoneException("unable to dial", e);
		}

		try {
			if (!ringLatch.await(10, TimeUnit.SECONDS)) {
				activeCallReq = null;
				ringLatch = null;
				rethrow("dial timeout");
			}
		} catch (InterruptedException e) {
			rethrow("interrupted", e);
		}
	}

	@Synchronized
	public void hangUp() {
		if (activeCallReq != null) {
			ua.terminate(activeCallReq);
			activeCallReq = null;
		} else {
		}
	}

	private void rethrow(String msg) {
		// logger.error(msg);
		System.out.println("error -- " + msg);
		throw new PhoneException(msg);
	}

	private void rethrow(String msg, Exception ex) {
		// logger.error(msg, ex);
		ex.printStackTrace();
		throw new PhoneException(msg, ex);
	}

	// Getters

	public String getUser() {
		return config.getUserPart();
	}

	public boolean isBusy() {
		return activeCallReq != null;
	}

	public SipRequest getActiveCall() {
		return activeCallReq;
	}

	class PhoneSipListener implements SipListener {

		@Override
		public void registering(SipRequest sipRequest) {
		}

		@Override
		public void registerSuccessful(SipResponse sipResponse) {
			registerSuccess = true;
			registerLatch.countDown();
			// logger.debug("register ok");
		}

		@Override
		public void registerFailed(SipResponse sipResponse) {
			registerSuccess = false;
			registerLatch.countDown();
			if (sipResponse != null) {
				// logger.debug("register fail -- {}",
				// sipResponse.getReasonPhrase());
			}
		}

		@Override
		public void incomingCall(SipRequest sipRequest, SipResponse provResponse) {
			// isIncomingRing = true;
			//
			// activeCallReq = sipRequest;
			// dialog = ua.getDialogManager().getDialog(provResponse);
			//
			// listener.onIncomingCall(phone);
		}

		@Override
		public void remoteHangup(SipRequest sipRequest) {
			activeCallReq = null;
			System.out.println("remote hangup");
			listener.onRemoteHangup(phone);
		}

		@Override
		public void ringing(SipResponse sipResponse) {
			if (ringLatch != null) {
				System.out.println("is ringing");
				ringLatch.countDown();
			}
		}

		@Override
		public void calleePickup(SipResponse sipResponse) {
			if (ringLatch != null) {
				// may have skipped ringing
				System.out.println("skipped ring, is picked up");
				ringLatch.countDown();
			} else {
				System.out.println("pick up");
			}

			// recordCall(sipResponse);
			// listener.onPickup(phone);
		}

		@Override
		public void error(SipResponse sipResponse) {
			// logger.error("error sip!");
			System.out.println("error sip -- " + sipResponse.getReasonPhrase());

			if (ringLatch != null) {
				ringLatch.countDown();
			}

			listener.onError(phone, sipResponse);
			activeCallReq = null;
		}
	}

	@Synchronized
	public void close() {
		this.ua.close();
	}

	public void recordCall() {

		  JavaxSoundManager javaxSoundManager = (JavaxSoundManager)
		 ua.getSoundManager();
		  byte[] readData = javaxSoundManager.readData();
		// javaxSoundManager.init();
		// javaxSoundManager.readData();
		// ByteArrayOutputStream byteArrayOutputStream = new
		// ByteArrayOutputStream();
		// byteArrayOutputStream.write(b);
		MidDialogRequestManager midDialogRequestManager = ua.getUas()
				.getMidDialogRequestManager();
		midDialogRequestManager.getClass();
		DialogManager dialogManager = ua.getDialogManager();
		Dialog dialog = dialogManager.getDialogCollection().iterator().next();
//		Dialog dialog = dialogManager.getDialog(activeCallReq);
		
		MediaManager mediaManager = ua.getMediaManager();
		FileReader fileReader = mediaManager.getFileReader();
		if (readData !=null) {
			WaveFileManager waveFileManager = new WaveFileManager();
			Wave wave = new Wave(new WaveHeader(), readData);
			waveFileManager.setWave(wave);
			waveFileManager.saveWaveAsFile("callRecord.wav");
		}
	}
}