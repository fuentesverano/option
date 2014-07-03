package option;

import net.sourceforge.peers.sip.transport.SipResponse;

public class DummyPhoneListener implements PhoneListener {

	@Override
	public void onIncomingCall(Phone phone) {
	}

	@Override
	public void onRemoteHangup(Phone phone) {
		// TODO Auto-generated method stub
		System.out.println("Cortooooooooooooooooooooooooooo");
	}

	@Override
	public void onPickup(Phone phone) {
		// TODO Auto-generated method stub
		System.out.println("Atendioooooooooooooooooooooo");
	}

	@Override
	public void onError(Phone phone, SipResponse sipResponse) {
		// TODO Auto-generated method stub
		System.out.println("Errorrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
	}

}
