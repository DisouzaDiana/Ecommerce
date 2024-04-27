import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Ordertransactoin extends JFrame {
    private JTextField tfUserId, tfItemId, tfQuantity, tfScratchCardId;
    private JButton btnPlaceOrder;
    private JTextArea taOrderStatus;
    private Connection connection;
    private JButton btnNewButton;

    public Ordertransactoin() {
        super("Order Transaction");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        tfUserId = new JTextField(20);
        tfItemId = new JTextField(20);
        tfQuantity = new JTextField(20);
        tfScratchCardId = new JTextField(20);

        btnPlaceOrder = new JButton("Place Order");

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("User ID:"));
        inputPanel.add(tfUserId);
        inputPanel.add(new JLabel("Item ID:"));
        inputPanel.add(tfItemId);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(tfQuantity);
        inputPanel.add(new JLabel("Scratch Card ID (optional):"));
        inputPanel.add(tfScratchCardId);
        inputPanel.add(new JLabel(""));
        inputPanel.add(btnPlaceOrder);

        taOrderStatus = new JTextArea(10, 40);
        JScrollPane scrollPane = new JScrollPane(taOrderStatus);

        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        btnNewButton = new JButton("Home page");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		dispose();
        		
        		Homepage hp = new Homepage();
        		hp.show();
        	}
        });
        scrollPane.setColumnHeaderView(btnNewButton);

        btnPlaceOrder.addActionListener(e -> placeOrder());

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

    private void placeOrder() {
        try {
            int userId = Integer.parseInt(tfUserId.getText());
            int itemId = Integer.parseInt(tfItemId.getText());
            int quantity = Integer.parseInt(tfQuantity.getText());
            int scratchCardId = tfScratchCardId.getText().isEmpty() ? -1 : Integer.parseInt(tfScratchCardId.getText());

            // Check if the user is valid and active
            if (isUserValidAndActive(userId)) {
                // Calculate order amount
                double orderAmount = calculateOrderAmount(itemId, quantity, scratchCardId);

                // Generate unique order reference
                String orderRef = generateOrderReference();

                // Insert order into database
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO Orders (UserID, ItemID, Quantity, ScratchCardID, Amount, OrderRef) " +
                                "VALUES (?, ?, ?, ?, ?, ?)"
                );
                statement.setInt(1, userId);
                statement.setInt(2, itemId);
                statement.setInt(3, quantity);
                statement.setInt(4, scratchCardId);
                statement.setDouble(5, orderAmount);
                statement.setString(6, orderRef);
                statement.executeUpdate();
                statement.close();

                taOrderStatus.setText("Order placed successfully. Order reference: " + orderRef);
            } else {
                taOrderStatus.setText("Error: Invalid or inactive user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isUserValidAndActive(int userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT IsActive FROM Users WHERE UserID = ?"
        );
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();
        boolean isValidAndActive = resultSet.next() && resultSet.getBoolean("IsActive");
        statement.close();
        return isValidAndActive;
    }

    private double calculateOrderAmount(int itemId, int quantity, int scratchCardId) throws SQLException {
        double itemPrice = getItemPrice(itemId);
        double discount = 0.0;

        // Apply discount if scratch card is provided and valid
        if (scratchCardId != -1 && isScratchCardValid(scratchCardId)) {
            discount = getScratchCardDiscount(scratchCardId);
        }

        return itemPrice * quantity * (1 - discount / 100.0);
    }

    private double getItemPrice(int itemId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT Price FROM Items WHERE ItemID = ?"
        );
        statement.setInt(1, itemId);
        ResultSet resultSet = statement.executeQuery();
        double itemPrice = resultSet.next() ? resultSet.getDouble("Price") : 0.0;
        statement.close();
        return itemPrice;
    }

    private boolean isScratchCardValid(int scratchCardId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT IsActive FROM ScratchCards WHERE ScratchCardID = ?"
        );
        statement.setInt(1, scratchCardId);
        ResultSet resultSet = statement.executeQuery();
        boolean isValid = resultSet.next() && resultSet.getBoolean("IsActive");
        statement.close();
        return isValid;
    }

    private double getScratchCardDiscount(int scratchCardId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT Discount FROM ScratchCards WHERE ScratchCardID = ?"
        );
        statement.setInt(1, scratchCardId);
        ResultSet resultSet = statement.executeQuery();
        double discount = resultSet.next() ? resultSet.getDouble("Discount") : 0.0;
        statement.close();
        return discount;
    }

    private String generateOrderReference() {
        // Generate a unique order reference (e.g., using current timestamp or UUID)
        return "ORD" + System.currentTimeMillis();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Ordertransactoin().setVisible(true);
        });
    }
}
