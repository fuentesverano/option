package option;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.Dimension;

public class OutputFrame extends JFrame {

	private JPanel contentPane;
	public JProgressBar progressBar;
	public JTextPane textPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OutputFrame frame = new OutputFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public OutputFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		progressBar = new JProgressBar();
		progressBar.setPreferredSize(new Dimension(146, 30));
		progressBar.setMaximumSize(new Dimension(32767, 40));
		progressBar.setFont(new Font("Tahoma", Font.PLAIN, 17));
		contentPane.add(progressBar, BorderLayout.NORTH);
		
		textPane = new JTextPane();
		textPane.setContentType("text/html");
		textPane.setEditable(false);
		contentPane.add(textPane, BorderLayout.CENTER);
	}

}
