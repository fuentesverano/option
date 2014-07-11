package option;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.peers.JavaConfig;
import net.sourceforge.peers.media.MediaMode;

public class PhoneManager {

	public enum Result{
		VALID, POSSIBLE, INVALID;
	
		public static Result getResult(float similarity){
			if (similarity < 0.25) {
				return Result.VALID;
			}
			if (similarity < 0.50) {
				return Result.POSSIBLE;
			}
			return INVALID;
		}
		
	}
	private Phone phone;
	private String prefix;
	private OutputFrame outputFrame;
	
	private static PhoneManager instance;
	
	private PhoneManager(){
		this.phone = new Phone(this.createJavaConfig());
	};
	
	public static PhoneManager getInstance(){
		if(instance==null){
			instance = new PhoneManager();
		}
		return instance;
	}
	
	public void callConsecutive(String prefix, String first, int cant, boolean persist){
		List<String> list = new ArrayList<String>();
	}
	
	public void callList(String prefix, List<String> list, boolean persist){
		
	}
	
	public void callAndAnalize(List<String> numberList, boolean persist){
		
		// register phone
		this.phone.register();
		
		for (String number : numberList) {
			try {
				this.phone.dial(number);
				Thread.sleep(15000);
				phone.hangUp();
				Result result = phone.validateNumber(number, persist);
				this.addResult(number, result);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// unregister phone
		this.phone.unregister();
		
		this.finish();
	}
	
	public void addResult(String number, Result result){
		
	}
	
	public void finish(){
		
	}
	
	/*******************************************************************************/
	
	private JavaConfig createJavaConfig() {
		try {
			JavaConfig javaConfig = new JavaConfig();
			
			// user configuration
			javaConfig.setUserPart("6002");
			javaConfig.setPassword("unsecurepassword");

			javaConfig.setDomain("192.168.1.102");
			javaConfig.setLocalInetAddress(InetAddress
					.getByName("192.168.1.104"));
			javaConfig.setMediaDebug(false);
			javaConfig.setMediaMode(MediaMode.captureAndPlayback);
			javaConfig.setOutboundProxy(null);
			javaConfig.setPublicInetAddress(null);
			return javaConfig;
		} catch (Exception e) {
			return null;
		}
	}
}
