import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*
;public class jlogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtuser;
	private JPasswordField txtpass;

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the frame.
	 */
	public jlogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setBounds(79, 58, 93, 16);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setBounds(84, 121, 61, 16);
		contentPane.add(lblNewLabel_1);
		
		txtuser = new JTextField();
		txtuser.setBounds(157, 53, 130, 26);
		contentPane.add(txtuser);
		txtuser.setColumns(10);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			
			
   public void actionPerformed(ActionEvent e) {
				
				String username = txtuser.getText();
				String password = txtpass.getText();
				
				
	
				try {
					
				
				
					if (username.isEmpty()||password.isEmpty()) {
					
						JOptionPane.showMessageDialog(btnNewButton, "Username/Password should not be empty", "Error", JOptionPane.ERROR_MESSAGE);	
					
						}
					
					Class.forName("com.mysql.cj.jdbc.Driver");
					
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ScratchCard?allowPublicKeyRetrieval=true&useSSL=false","root","mysql");
					
					System.out.println(con);
					
					if(con != null) {
						System.out.println("Connected to db");
					}
					Statement st = con.createStatement();
					
					String sql ="select * from user_login where username = '"+ username+"' and password = '"+password+"'";
					System.out.println(sql);
					ResultSet rs = st.executeQuery(sql);
					System.out.println(rs);
					//JOptionPane.showMessageDialog(null, "login successful");
					
					if(!rs.next()) {
						
						System.out.println("no data");
						JOptionPane.showMessageDialog(null, "Incorrect UserID or Password, try again!");
						
						
					
					}
					else {
						if(username.equals("admin")) {
							dispose();
							
							System.out.println("inside else");
							Homepage hpage = new Homepage();
							
							hpage.show();
							
							
						}
						else {
							System.out.println("Inside homepage 2");
							dispose();
							Homepage2 hpage2 = new Homepage2();
							hpage2.show();
						}
						
						
					}
					
					con.close();
				
				}catch(Exception ex) {
					
					System.out.println(ex.getMessage());
				}
				
				
			}
		});
		btnNewButton.setBounds(157, 167, 117, 29);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Reset");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				txtuser.setText(" ");
				txtpass.setText(" ");
			}
			 
			
		});
		btnNewButton_1.setBounds(298, 167, 117, 29);
		contentPane.add(btnNewButton_1);
		
		txtpass = new JPasswordField();
		txtpass.setBounds(154, 116, 130, 26);
		contentPane.add(txtpass);
	}
}
