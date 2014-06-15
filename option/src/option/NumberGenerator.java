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
import javax.telephony.InvalidArgumentException;
import javax.telephony.InvalidPartyException;
import javax.telephony.InvalidStateException;
import javax.telephony.JtapiPeer;
import javax.telephony.JtapiPeerFactory;
import javax.telephony.JtapiPeerUnavailableException;
import javax.telephony.MethodNotSupportedException;
import javax.telephony.PrivilegeViolationException;
import javax.telephony.Provider;
import javax.telephony.ResourceUnavailableException;
import javax.telephony.Terminal;
import javax.telephony.callcontrol.CallControlCall;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;

import com.headissue.asterisk.jtapi.gjtapi.AsteriskProvider;

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
				} catch (JtapiPeerUnavailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvalidArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ResourceUnavailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvalidStateException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (PrivilegeViolationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MethodNotSupportedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvalidPartyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		generate.setFont(new Font("Dialog", Font.BOLD, 14));
		generate.setBounds(89, 68, 104, 25);
		frmGeneradorDeNumeros.getContentPane().add(generate);
	}

	public void generateValidNumbers() throws IllegalStateException,
			IOException, AuthenticationFailedException, TimeoutException,
			InterruptedException, JtapiPeerUnavailableException,
			InvalidArgumentException, ResourceUnavailableException,
			InvalidStateException, PrivilegeViolationException,
			MethodNotSupportedException, InvalidPartyException {

		ManagerConnectionFactory managerConnectionFactory = new ManagerConnectionFactory(
				"172.16.100.93", "admin", "amp111");
		ManagerConnection managerConnection = managerConnectionFactory
				.createManagerConnection();

		// connect to Asterisk and log in
		managerConnection.login();
		System.out.println("Login OK");

		OriginateAction originateAction = new OriginateAction();
		originateAction.setActionId("Call#1");
		originateAction.setData("Atende gato...");
		originateAction.setApplication("NumberGenerator");

		originateAction.setChannel("SIP/6001");
		originateAction.setCallerId("6001");
		originateAction.setContext("from-internal");
		originateAction.setExten("100");
		originateAction.setPriority(new Integer(1));
		originateAction.setTimeout(new Long(60000));

		// send the originate action and wait for a maximum of 30 seconds for
		// Asterisk
		// to send a reply
		ManagerResponse originateResponse = managerConnection.sendAction(
				originateAction, 60000);

		// print out whether the originate succeeded or not
		System.out.println(originateResponse.getResponse());
		System.out.println(originateResponse.getMessage());

		// and finally log off and disconnect
		managerConnection.logoff();
	}
}
