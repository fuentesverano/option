package option;

import java.awt.Color;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

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
	private OutputFrame console;
	private int cantCalls;
	private int processedCalls;
	private static PhoneManager instance;
	
	private PhoneManager(){
		this.phone = new Phone(this.createJavaConfig());
		this.phone.register();
	};
	
	public static PhoneManager getInstance(){
		if(instance==null){
			instance = new PhoneManager();
		}
		return instance;
	}
	
	public void callConsecutive(String prefix, String first, int cant, boolean persist, OutputFrame console){
		this.console = console;
		List<String> res = new ArrayList<String>();
		String realFirst = prefix+first;
		String zeros = "";
		while(realFirst.startsWith("0")){
			zeros+="0";
			realFirst = realFirst.substring(1);
		}
		String currentNumber = realFirst;
		for(int i = 0; i<cant; i++){
			String toAdd = zeros + currentNumber;
			res.add(toAdd);
			currentNumber = String.valueOf(Integer.parseInt(currentNumber) + 1);
		}
		callAndAnalize(res, persist);
	}
	
	public void callList(String prefix, String inputNumbers, boolean persist, OutputFrame console){
		this.console = console;
		String[] split = inputNumbers.split(",|\\s|/n");
		List<String> res = new ArrayList<String>();
		for (String elem: split){
			if (!elem.trim().equals("")){
				res.add(prefix+elem);
			}
		}
		callAndAnalize(res, persist);
	}
	
	public void callAndAnalize(List<String> numberList, boolean persist){
		this.cantCalls=numberList.size();
		// register phone

		
		for (String number : numberList) {
			try {
				this.addProcessing(number);
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
	
	public void addProcessing(String number){
//		this.console.getProgressBar().setValue((processedCalls/cantCalls)*100);
//		String text = this.console.textPane.getText();
//		String replace = (!text.equals(""))? text + "/nProcesando... "+number : "Procesando... "+number;
//		this.console.textPane.setVisible(false);
//		this.console.textPane.setText(replace);
//		this.console.textPane.setVisible(true);
		JTextPane textPane = this.console.textPane;
		console.setVisible(false);
		
		StyledDocument doc = textPane.getStyledDocument();
		Style style = textPane.addStyle("I'm a Style", null);
		StyleConstants.setForeground(style, Color.black);

		try {
			doc.insertString(doc.getLength(), "<h1>Procesando... </h1>"+number, style);
		} catch (BadLocationException e) {
		}
		console.setVisible(true);
		
//		StringBuilder buildSomething;
//		textPane.setText(buildSomething.toString());
	}
	
	public void addResult(String number, Result result){
//		this.console.getProgressBar().setValue((processedCalls/cantCalls)*100);
//		String replace = this.console.textPane.getText().replace("Procesando... "+number, "- " + number + "/t" + result.name() );
//		this.console.textPane.setVisible(false);
//		this.console.textPane.setText(replace);
//		this.console.textPane.setVisible(true);
		
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

			javaConfig.setDomain("192.168.1.106");
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
