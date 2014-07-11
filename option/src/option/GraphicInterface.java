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

import javax.swing.JCheckBox;

public class GraphicInterface extends JFrame {

	private JPanel contentPane;
	private JTextField textField_first;
	private JTextField textField_quantity;
	private JTextField textField;
	private JTextField textField_prefix_list;
	private JCheckBox chckbx_grabar_consecutive;
	private JTextArea textArea_input_list;
	private JCheckBox chckbx_grabar_list;
	private JButton button_list;

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
		setBounds(100, 100, 327, 461);
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
				boolean persist = chckbx_grabar_consecutive.isSelected();
				String first = textField_first.getText();
				String prefix = textField.getText();
				OutputFrame console = new OutputFrame();
				console.setVisible(true);
				instance.callConsecutive(prefix, first, cant, persist,console);
			}
		});
		
		JLabel lblPrefijo = new JLabel("Prefijo:");
		lblPrefijo.setFont(new Font("Tahoma", Font.BOLD, 14));
		tab_consecutive.add(lblPrefijo, "4, 8, right, default");
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tab_consecutive.add(textField, "6, 8, left, default");
		textField.setColumns(10);
		
		JLabel lblGrabarLlamadas = new JLabel("Grabar llamadas:");
		lblGrabarLlamadas.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGrabarLlamadas.setFont(new Font("Tahoma", Font.BOLD, 14));
		tab_consecutive.add(lblGrabarLlamadas, "4, 10");
		
		chckbx_grabar_consecutive = new JCheckBox("");
		chckbx_grabar_consecutive.setHorizontalTextPosition(SwingConstants.LEFT);
		chckbx_grabar_consecutive.setHorizontalAlignment(SwingConstants.LEFT);
		chckbx_grabar_consecutive.setFont(new Font("Tahoma", Font.BOLD, 14));
		tab_consecutive.add(chckbx_grabar_consecutive, "6, 10");
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
		
		textArea_input_list = new JTextArea();
		textArea_input_list.setBounds(10, 48, 276, 262);
		tab_list.add(textArea_input_list);
		
		JLabel lblNewLabel_1 = new JLabel("Prefijo:");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setBounds(10, 11, 58, 14);
		tab_list.add(lblNewLabel_1);
		
		textField_prefix_list = new JTextField();
		textField_prefix_list.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textField_prefix_list.setBounds(77, 10, 43, 20);
		tab_list.add(textField_prefix_list);
		textField_prefix_list.setColumns(10);
		
		chckbx_grabar_list = new JCheckBox("Grabar llamadas: ");
		chckbx_grabar_list.setHorizontalTextPosition(SwingConstants.LEFT);
		chckbx_grabar_list.setFont(new Font("Tahoma", Font.BOLD, 14));
		chckbx_grabar_list.setHorizontalAlignment(SwingConstants.RIGHT);
		chckbx_grabar_list.setBounds(126, 7, 164, 23);
		tab_list.add(chckbx_grabar_list);
		
		button_list = new JButton("");
		button_list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				PhoneManager instance = PhoneManager.getInstance();
				boolean persist = chckbx_grabar_list.isSelected();
				String inputNumbers = textArea_input_list.getText();
				String prefix = textField_prefix_list.getText();
				OutputFrame console = new OutputFrame();
				console.setVisible(true);
				
				instance.callList(prefix, inputNumbers, persist,console);
			}
		});
		button_list.setIcon(new ImageIcon(GraphicInterface.class.getResource("/images/iconButton2.png")));
		button_list.setVerticalTextPosition(SwingConstants.BOTTOM);
		button_list.setVerticalAlignment(SwingConstants.TOP);
		button_list.setToolTipText("Comenzar!");
		button_list.setHorizontalTextPosition(SwingConstants.CENTER);
		button_list.setHorizontalAlignment(SwingConstants.LEFT);
		button_list.setBounds(209, 321, 77, 53);
		tab_list.add(button_list);
	}
}
