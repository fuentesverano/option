package option;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class GraphicInterface extends JFrame {

	private JPanel contentPane;
	private JTextField textField_first;
	private JTextField textField_quantity;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphicInterface frame = new GraphicInterface();
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
	public GraphicInterface() {
		setTitle("Obdulio phone validator");
		setIconImage(Toolkit.getDefaultToolkit().getImage(GraphicInterface.class.getResource("/images/icon.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 461);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel tab_consecutive = new JPanel();
		tabbedPane.addTab("Consecutivos", null, tab_consecutive, "Llamar una cierta cantidad de numeros consecutivos a partir de un numero dado");
		tab_consecutive.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JLabel lblNumeroBase = new JLabel("Numero base:");
		lblNumeroBase.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNumeroBase.setFont(new Font("Tahoma", Font.BOLD, 14));
		tab_consecutive.add(lblNumeroBase, "4, 4, right, default");
		
		textField_first = new JTextField();
		textField_first.setHorizontalAlignment(SwingConstants.LEFT);
		textField_first.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tab_consecutive.add(textField_first, "6, 4, left, default");
		textField_first.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Cantidad:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		tab_consecutive.add(lblNewLabel, "4, 6, right, default");
		
		textField_quantity = new JTextField();
		textField_quantity.setHorizontalAlignment(SwingConstants.LEFT);
		textField_quantity.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textField_quantity.setColumns(10);
		tab_consecutive.add(textField_quantity, "6, 6, left, default");
		
		JButton btnNewButton = new JButton("");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int cant = new Integer(textField_quantity.getText());
				PhoneManager instance = PhoneManager.getInstance();
				instance.callConsecutive(textField.getText(),textField_first.getText(), cant,true);
			}
		});
		
		JLabel lblPrefijo = new JLabel("Prefijo:");
		lblPrefijo.setFont(new Font("Tahoma", Font.BOLD, 14));
		tab_consecutive.add(lblPrefijo, "4, 8, right, default");
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tab_consecutive.add(textField, "6, 8, left, default");
		textField.setColumns(10);
		btnNewButton.setVerticalAlignment(SwingConstants.TOP);
		btnNewButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnNewButton.setHorizontalTextPosition(SwingConstants.CENTER);
		btnNewButton.setToolTipText("Comenzar!");
		btnNewButton.setIcon(new ImageIcon(GraphicInterface.class.getResource("/images/iconButton2.png")));
		btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
		tab_consecutive.add(btnNewButton, "6, 12, center, top");
		
		JPanel tab_list = new JPanel();
		tabbedPane.addTab("Lista", null, tab_list, "Llamar a una lista de numeros");
		tab_list.setLayout(null);
		
		JTextArea textArea_input = new JTextArea();
		textArea_input.setBounds(10, 11, 249, 363);
		tab_list.add(textArea_input);
	}
}
