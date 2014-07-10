package option;

import java.util.ArrayList;
import java.util.List;

public class PhoneManager {

	public enum Result{
		VALID, POSSIBLE, INVALID;
	}
	private Phone phone;
	private String prefix;
	private OutputFrame outputFrame;
	
	private static PhoneManager instance;
	
	private PhoneManager(){
		//Aca se crea el Phone
		
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
	
	public void callAndAnalize(List<String> list, boolean persist){
		
	}
	
	public void addResult(String number, Result result, int score){
		
	}
	
	public void finish(){
		
	}
}
