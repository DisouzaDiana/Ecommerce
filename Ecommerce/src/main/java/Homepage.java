import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Homepage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Homepage frame = new Homepage();
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
	public Homepage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 764, 390);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Home-Admin");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 25));
		lblNewLabel.setBounds(292, 47, 292, 52);
		contentPane.add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Scratchcard Generation", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(83, 99, 398, 193);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setBounds(6, 61, 42, -24);
		panel.add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("Generate ScratchCard");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				dispose();
				
				SractchcardManager gs = new SractchcardManager();
				gs.show();
				
				
			}
		});
		btnNewButton.setBounds(98, 34, 195, 29);
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("AllocateScratchCard");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				dispose();
				AllocateScratchcardTo as = new AllocateScratchcardTo();
				as.show();
			}
			
		});
		btnNewButton_1.setBounds(98, 88, 195, 29);
		panel.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Order Transaction");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				dispose();
				
				Ordertransactoin ot = new Ordertransactoin();
				ot.show();
				
				
			}
		});
		btnNewButton_2.setBounds(98, 129, 185, 29);
		panel.add(btnNewButton_2);
	}
}
