
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AllocateScratchcardTo extends JFrame {
    private JTextField tfUserId, tfScratchCardId;
    private JButton btnAllocate;
    private JTextArea taAllocationStatus;
    private Connection connection;
    private JButton btnNewButton;

    public AllocateScratchcardTo() {
        super("Allocate Scratch Card to User");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        tfUserId = new JTextField(20);
        tfScratchCardId = new JTextField(20);

        btnAllocate = new JButton("Allocate Scratch Card");

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("User ID:"));
        inputPanel.add(tfUserId);
        inputPanel.add(new JLabel("Scratch Card ID:"));
        inputPanel.add(tfScratchCardId);
        inputPanel.add(new JLabel(""));
        inputPanel.add(btnAllocate);

        taAllocationStatus = new JTextArea(10, 40);
        JScrollPane scrollPane = new JScrollPane(taAllocationStatus);

        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        btnNewButton = new JButton("Home Page");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		dispose();
        		
        		Homepage hp = new Homepage();
        		hp.show();
        	}
        });
        scrollPane.setColumnHeaderView(btnNewButton);

        btnAllocate.addActionListener(e -> allocateScratchCard());

        pack();
        setLocationRelativeTo(null);

        // Connect to the MySQL database
        connectToDatabase();
    }

    private void connectToDatabase() {
        try {
            // Update the following with your database credentials
            String url = "jdbc:mysql://localhost:3306/ScratchCard";
            String username = "root";
            String password = "mysql";

            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void allocateScratchCard() {
        try {
            int userId = Integer.parseInt(tfUserId.getText());
            int scratchCardId = Integer.parseInt(tfScratchCardId.getText());

            // Check if the scratch card is unused
            if (!isScratchCardUsed(scratchCardId)) {
            	
            	System.out.println("Inside if not isScratchCardUsed");
                // Update user's scratch card ID
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE User SET ScratchCardGUIID = ? WHERE UserID = ?"
                );
                statement.setInt(1, scratchCardId);
                statement.setInt(2, userId);
                statement.executeUpdate();
                statement.close();

                taAllocationStatus.setText("Scratch card allocated to user successfully.");
            } else {
                taAllocationStatus.setText("Error: Scratch card is already used.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isScratchCardUsed(int scratchCardId) throws SQLException {
    	
    	System.out.println("Inside isScratchCardUsed");
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM User WHERE ScratchCardGUIID = ?"
        );
        statement.setInt(1, scratchCardId);
        ResultSet resultSet = statement.executeQuery();
        boolean isUsed = resultSet.next();
        statement.close();
        return isUsed;
    }

   /* public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AllocateScratchcard().setVisible(true);
        });
    }*/
}