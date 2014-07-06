package option;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import net.sourceforge.peers.JavaConfig;
import net.sourceforge.peers.media.MediaMode;

import com.musicg.fingerprint.FingerprintManager;
import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.fingerprint.FingerprintSimilarityComputer;
import com.musicg.wave.Wave;
import com.musicg.wave.WaveFileManager;
import com.musicg.wave.WaveHeader;

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
				generateValidNumbers();
			}
		});
		generate.setFont(new Font("Dialog", Font.BOLD, 14));
		generate.setBounds(89, 68, 104, 25);
		frmGeneradorDeNumeros.getContentPane().add(generate);
	}

	public void generateValidNumbers() {

		JavaConfig javaConfig = this.createJavaConfig();
		PhoneListener phoneListener = new DummyPhoneListener();
		Phone phone = new Phone(javaConfig, false, phoneListener);
		phone.register();
		phone.dial("6001");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		phone.hangUp();
		phone.recordCall();
		phone.unregister();
	}

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
//			javaConfig.setMediaFile("file");
			javaConfig.setOutboundProxy(null);
			javaConfig.setPublicInetAddress(null);
			return javaConfig;
		} catch (Exception e) {
			return null;
		}
	}
}
