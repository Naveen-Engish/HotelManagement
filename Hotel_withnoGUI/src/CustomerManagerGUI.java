import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class CustomerManagerGUI {
    private JFrame frame;
    private JTextField nameField, emailField, phoneField, addressField;
    private JTable customerTable;
    private DefaultTableModel tableModel;

    public CustomerManagerGUI() {
        frame = new JFrame("Customer Manager");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 500); // Increased size for better layout
        frame.getContentPane().setBackground(new Color(245, 245, 245)); // Off-white background
        frame.setLayout(new BorderLayout());

        // Create input fields for customer data
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10)); // Added spacing between components
        inputPanel.setBackground(new Color(245, 245, 245)); // Off-white input panel background

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.BLACK);
        nameField = new JTextField();
        nameField.setBackground(new Color(173, 216, 230)); // Light blue background

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.BLACK);
        emailField = new JTextField();
        emailField.setBackground(new Color(173, 216, 230)); // Light blue background

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setForeground(Color.BLACK);
        phoneField = new JTextField();
        phoneField.setBackground(new Color(173, 216, 230)); // Light blue background

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setForeground(Color.BLACK);
        addressField = new JTextField();
        addressField.setBackground(new Color(173, 216, 230)); // Light blue background

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(emailLabel);
        inputPanel.add(emailField);
        inputPanel.add(phoneLabel);
        inputPanel.add(phoneField);
        inputPanel.add(addressLabel);
        inputPanel.add(addressField);

        // Buttons for actions
        JButton addCustomerButton = createStyledButton("Add Customer");
        JButton viewCustomersButton = createStyledButton("View Customers");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245)); // Off-white button panel background
        buttonPanel.add(addCustomerButton);
        buttonPanel.add(viewCustomersButton);

        // Table for displaying customer data
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Customer ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Email");
        tableModel.addColumn("Phone");
        tableModel.addColumn("Address");

        customerTable = new JTable(tableModel);
        customerTable.setPreferredScrollableViewportSize(new Dimension(500, 200)); // Resize table for better display
        customerTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(customerTable);

        // Add components to frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        frame.setVisible(true);

        // Action listeners for buttons
        addCustomerButton.addActionListener(e -> addCustomerToDatabase());
        viewCustomersButton.addActionListener(e -> displayCustomers());
    }

    // Method to create a styled button
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(100, 149, 237)); // Button background color
        button.setForeground(Color.WHITE); // Button text color
        button.setFocusPainted(false); // Remove focus border
        button.setBorderPainted(false); // Remove border
        button.setOpaque(true); // Ensure button is opaque
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Font style and size
        button.setPreferredSize(new Dimension(160, 40)); // Larger button for better appearance
        return button;
    }

    // Method to add a customer to the database
    private void addCustomerToDatabase() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Customers (name, email, phone_number, address) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setString(4, address);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Customer added successfully!");
                clearFields();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error adding customer: " + ex.getMessage());
        }
    }

    // Method to display all customers in the table
    private void displayCustomers() {
        // Clear existing table data
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Customers";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            // Add customer data to the table
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("address")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error retrieving customers: " + ex.getMessage());
        }
    }

    // Clear input fields after adding a customer
    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerManagerGUI());
    }
}
