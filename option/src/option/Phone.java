package option;

import java.io.File;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import lombok.Synchronized;
import net.sourceforge.peers.Config;
import net.sourceforge.peers.FileLogger;
import net.sourceforge.peers.sip.core.useragent.SipListener;
import net.sourceforge.peers.sip.core.useragent.UserAgent;
import net.sourceforge.peers.sip.syntaxencoding.SipUriSyntaxException;
import net.sourceforge.peers.sip.transport.SipRequest;
import net.sourceforge.peers.sip.transport.SipResponse;
import option.PhoneManager.Result;

import com.musicg.fingerprint.FingerprintManager;
import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.fingerprint.FingerprintSimilarityComputer;
import com.musicg.wave.Wave;
import com.musicg.wave.WaveFileManager;
import com.musicg.wave.WaveHeader;

public class Phone {

	private UserAgent ua;
	private boolean registerSuccess;
	private Config config;
	private CountDownLatch registerLatch;
	private CountDownLatch ringLatch;
	private SipRequest activeCallReq;
	private static Set<byte[]> invalidFingerprints = new HashSet<byte[]>();

	public Phone(Config config) {
		this.config = config;
		try {
			File folder = new File("invalidPatterns/");
			File[] files = folder.listFiles();
			for (File file : files) {
				Wave wavePattern = new Wave(file.getPath());
				FingerprintManager fingerprintManager = new FingerprintManager();
				byte[] fingerprint = fingerprintManager
						.extractFingerprint(wavePattern);
				invalidFingerprints.add(fingerprint);
			}
			ua = new UserAgent(new PhoneSipListener(), config, new FileLogger(
					null), new RecordManager());
		} catch (SocketException e) {
			throw new PhoneException("Failed creating Phone", e);
		}
	}

	@Synchronized
	public void register() {
		this.registerSuccess = false;
		this.registerLatch = new CountDownLatch(1);
		boolean completed = false;
		try {
			ua.register();
			completed = registerLatch.await(5, TimeUnit.SECONDS);
			if (completed) {
				if (!registerSuccess) {
					throw new PhoneException(
							"Register User Agent with asterisk fail");
				}
			} else {
				throw new PhoneException("Register timeout");
			}
		} catch (Exception e) {
			throw new PhoneException("Register User Agent with asterisk fail",
					e);
		}
	}

	@Synchronized
	public void unregister() {
		try {
			ua.unregister();
		} catch (SipUriSyntaxException e) {
			throw new PhoneException(
					"Unregister User Agent with asterisk fail", e);
		}
	}

	@Synchronized
	public void reset() {
		try {
			ua.close();
		} catch (Exception e) {
			throw new PhoneException("Reset User Agent with asterisk fail", e);
		}
		registerLatch = null;
		ringLatch = null;
		activeCallReq = null;
		try {
			ua = new UserAgent(new PhoneSipListener(), config, new FileLogger(
					null), new RecordManager());
			ua.register();
		} catch (Exception e) {
			throw new PhoneException("Reset phone fail", e);
		}
	}

	@Synchronized
	public void dial(String extension) {
		String sipAddr = "sip:" + extension + "@" + config.getDomain();
		dialSip(sipAddr);
	}

	@Synchronized
	public void dialSip(String sipAddr) {
		if (activeCallReq != null) {
			throw new PhoneException("busy with another call");
		}
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
			}
		} catch (InterruptedException e) {
			throw new PhoneException("InterruptedException", e);
		}
	}

	@Synchronized
	public void hangUp() {
		if (activeCallReq != null) {
			ua.terminate(activeCallReq);
			activeCallReq = null;
		}
	}

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
		}

		@Override
		public void registerFailed(SipResponse sipResponse) {
			registerSuccess = false;
			registerLatch.countDown();
			if (sipResponse != null) {
				throw new PhoneException(sipResponse.getReasonPhrase());
			}
		}

		@Override
		public void incomingCall(SipRequest sipRequest, SipResponse provResponse) {
		}

		@Override
		public void remoteHangup(SipRequest sipRequest) {
			activeCallReq = null;
		}

		@Override
		public void ringing(SipResponse sipResponse) {
			if (ringLatch != null) {
				ringLatch.countDown();
			}
		}

		@Override
		public void calleePickup(SipResponse sipResponse) {
			if (ringLatch != null) {
				// may have skipped ringing
				ringLatch.countDown();
			}
		}

		@Override
		public void error(SipResponse sipResponse) {
			System.out.println("error sip -- " + sipResponse.getReasonPhrase());
			if (ringLatch != null) {
				ringLatch.countDown();
			}
			activeCallReq = null;
		}
	}

	@Synchronized
	public void close() {
		this.ua.close();
	}

	public Result validateNumber(String number, boolean persistCall) {

		float bestSimilarity = 0;
		StringBuilder log = new StringBuilder("Number: " + number);

		RecordManager recordManager = (RecordManager) ua.getSoundManager();
		byte[] readData = recordManager.readData();
		if (readData != null) {
			Wave wave = new Wave(new WaveHeader(), readData);
			if (persistCall) {
				WaveFileManager waveFileManager = new WaveFileManager();
				waveFileManager.setWave(wave);
				waveFileManager
						.saveWaveAsFile("recordCalls/" + number + ".wav");
			}

			FingerprintManager fingerprintManager = new FingerprintManager();
			byte[] fingerprint = fingerprintManager.extractFingerprint(wave);

			for (byte[] invalidFingerprint : invalidFingerprints) {
				FingerprintSimilarityComputer fingerprintSimilarityComputer = new FingerprintSimilarityComputer(
						fingerprint, invalidFingerprint);
				FingerprintSimilarity fingerprintsSimilarity = fingerprintSimilarityComputer
						.getFingerprintsSimilarity();
				float similarity = fingerprintsSimilarity.getSimilarity();
				log.append("--");
				log.append(similarity);
				bestSimilarity = similarity > bestSimilarity ? similarity
						: bestSimilarity;
			}

			System.out.println(log.toString());
			Result.getResult(bestSimilarity);
		}
		return Result.INVALID;
	}
}