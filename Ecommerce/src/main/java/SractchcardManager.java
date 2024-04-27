import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class SractchcardManager extends JFrame {
    private JTextField tfDiscount, tfExpiryDays, tfNumOfCards;
    private JButton btnGenerate;
    private JTextArea taScratchCards;
    private Connection connection;
    private JButton btnNewButton;

    public SractchcardManager() {
        super("Scratch Card Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        tfDiscount = new JTextField(20);
        tfExpiryDays = new JTextField(20);
        tfNumOfCards = new JTextField(20);

        btnGenerate = new JButton("Generate Scratch Cards");

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Discount:"));
        inputPanel.add(tfDiscount);
        inputPanel.add(new JLabel("Expiry Days:"));
        inputPanel.add(tfExpiryDays);
        inputPanel.add(new JLabel("Number of Cards:"));
        inputPanel.add(tfNumOfCards);
        
        btnNewButton = new JButton("Home Page");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		dispose();
        		Homepage hp= new Homepage();
        		hp.show();
        	}
        });
        inputPanel.add(btnNewButton);
        inputPanel.add(new JLabel(""));
        inputPanel.add(btnGenerate);

        taScratchCards = new JTextArea(10, 40);
        JScrollPane scrollPane = new JScrollPane(taScratchCards);

        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        btnGenerate.addActionListener(e -> generateScratchCards());

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

    private void generateScratchCards() {
        try {
            double discount = Double.parseDouble(tfDiscount.getText());
            int expiryDays = Integer.parseInt(tfExpiryDays.getText());
            int numOfCards = Integer.parseInt(tfNumOfCards.getText());

            // Deactivate existing unused scratch cards
            deactivateUnusedScratchCards();

            // Generate new scratch cards
            for (int i = 0; i < numOfCards; i++) {
                LocalDate expiryDate = LocalDate.now().plusDays(expiryDays);

                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO ScratchCards (Discount, ScratchCardExpiryDate, IsActive) " +
                                "VALUES (?, ?, ?)"
                );
                statement.setDouble(1, discount);
                statement.setDate(2, Date.valueOf(expiryDate));
                statement.setBoolean(3, true);
                statement.executeUpdate();
                statement.close();
            }

            taScratchCards.setText(numOfCards + " Scratch Card(s) generated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deactivateUnusedScratchCards() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE ScratchCards SET IsActive = false WHERE IsActive = true"
        );
        statement.executeUpdate();
        statement.close();
    }

  /*  public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ScratchCardManager().setVisible(true);
        });
    }*/
}

