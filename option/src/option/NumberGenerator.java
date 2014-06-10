package option;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.telephony.Call;
import javax.telephony.InvalidArgumentException;
import javax.telephony.InvalidPartyException;
import javax.telephony.InvalidStateException;
import javax.telephony.JtapiPeer;
import javax.telephony.JtapiPeerFactory;
import javax.telephony.JtapiPeerUnavailableException;
import javax.telephony.MethodNotSupportedException;
import javax.telephony.PrivilegeViolationException;
import javax.telephony.Provider;
import javax.telephony.ProviderUnavailableException;
import javax.telephony.ResourceUnavailableException;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;

import com.headissue.asterisk.jtapi.AsteriskJtapiProvider;

public class NumberGenerator {

	private JFrame frmGeneradorDeNumeros;
	private JTextField baseNumber;
	private JTextField amount;
	private JButton generate;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NumberGenerator window = new NumberGenerator();
					window.frmGeneradorDeNumeros.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public NumberGenerator() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGeneradorDeNumeros = new JFrame();
		frmGeneradorDeNumeros.setFont(new Font("Dialog", Font.BOLD, 14));
		frmGeneradorDeNumeros.setTitle("Generador de Números");
		frmGeneradorDeNumeros.setBounds(100, 100, 289, 365);
		frmGeneradorDeNumeros.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGeneradorDeNumeros.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Número Base :");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel.setBounds(24, 12, 127, 15);
		frmGeneradorDeNumeros.getContentPane().add(lblNewLabel);
		
		baseNumber = new JTextField();
		baseNumber.setFont(new Font("Dialog", Font.BOLD, 14));
		baseNumber.setBounds(150, 10, 94, 19);
		frmGeneradorDeNumeros.getContentPane().add(baseNumber);
		baseNumber.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Cantidad :");
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel_1.setBounds(59, 39, 82, 17);
		frmGeneradorDeNumeros.getContentPane().add(lblNewLabel_1);
		
		amount = new JTextField();
		amount.setFont(new Font("Dialog", Font.BOLD, 14));
		amount.setBounds(150, 39, 43, 19);
		frmGeneradorDeNumeros.getContentPane().add(amount);
		amount.setColumns(10);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(22, 105, 238, 19);
		frmGeneradorDeNumeros.getContentPane().add(progressBar);
		
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setBounds(59, 136, 173, 176);
		frmGeneradorDeNumeros.getContentPane().add(textPane);
		
		generate = new JButton("Generar");
		generate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Click");
				try {
					generateValidNumbers();
				} catch (IllegalStateException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (AuthenticationFailedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (TimeoutException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		generate.setFont(new Font("Dialog", Font.BOLD, 14));
		generate.setBounds(89, 68, 104, 25);
		frmGeneradorDeNumeros.getContentPane().add(generate);
	}
	
	public void generateValidNumbers() throws IllegalStateException, IOException, AuthenticationFailedException, TimeoutException, InterruptedException{

		try {
			
			
//			AsteriskJtapiProvider asteriskJtapiProvider = new AsteriskJtapiProvider();
//			CallId callId = asteriskJtapiProvider.createCall(new CallerImpl() , "192.168.1.250", "1102", "095763885");
////			AsteriskCall asteriskCall = new AsteriskCall();
////			createCall(net.sourceforge.gjtapi.CallId _callId,
////                    java.lang.String _addr,
////                    java.lang.String _terminal,
////                    java.lang.String _destination)  
//			AsteriskJtapiProvider asteriskJtapiProvider = new AsteriskJtapiProvider();
//			asteriskJtapiProvider.createCall(null, "192.168.1.250", "1102", "9095763885");
			JtapiPeer peer = JtapiPeerFactory.getJtapiPeer(null);
			Provider provider = peer.getProvider("Asterisk;Server=192.168.1.250;Login=1102;Password=1102pwd");
			Call call = provider.createCall();
			
//			// Realizamos una llamada  
            call.connect(provider.getTerminal("1102"), provider.getAddress("192.168.1.250"), "9095763885");  
//            
//            // Finalizamos el proveedor
            provider.shutdown();
			
		} catch (ProviderUnavailableException e) {
			// TODO: handle exception
			System.out.println(e.getCause());
		} catch (ResourceUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PrivilegeViolationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getCause());
		} catch (InvalidPartyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MethodNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JtapiPeerUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	
}
