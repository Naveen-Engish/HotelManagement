import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FeedbackManagerGUI {
    private JFrame frame;
    private JTextField customerIdField;
    private JTextField roomNumberField;
    private JTextArea feedbackArea;
    private JTextArea displayArea;

    public FeedbackManagerGUI() {
        frame = new JFrame("Feedback Manager");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 450);
        frame.getContentPane().setBackground(new Color(245, 245, 245)); // Off-white background

        // Fields for entering feedback
        JLabel customerIdLabel = new JLabel("Customer ID:");
        customerIdLabel.setForeground(Color.BLACK);
        customerIdField = new JTextField(10);
        styleTextField(customerIdField);

        JLabel roomNumberLabel = new JLabel("Room Number:");
        roomNumberLabel.setForeground(Color.BLACK);
        roomNumberField = new JTextField(10);
        styleTextField(roomNumberField);

        JLabel feedbackLabel = new JLabel("Feedback:");
        feedbackLabel.setForeground(Color.BLACK);
        feedbackArea = new JTextArea(5, 20);
        styleTextArea(feedbackArea);

        JButton submitButton = new JButton("Submit Feedback");
        styleButton(submitButton);
        submitButton.addActionListener(e -> submitFeedback());

        JButton viewFeedbackButton = new JButton("View Feedback");
        styleButton(viewFeedbackButton);
        viewFeedbackButton.addActionListener(e -> viewFeedback());

        // Area to display feedback details
        displayArea = new JTextArea(10, 40);
        displayArea.setEditable(false);
        styleTextArea(displayArea);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10)); // Added space between components
        inputPanel.setBackground(new Color(245, 245, 245)); // Background for input panel
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Added padding
        inputPanel.add(customerIdLabel);
        inputPanel.add(customerIdField);
        inputPanel.add(roomNumberLabel);
        inputPanel.add(roomNumberField);
        inputPanel.add(feedbackLabel);
        inputPanel.add(new JScrollPane(feedbackArea));
        inputPanel.add(submitButton);
        inputPanel.add(viewFeedbackButton);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(displayArea), BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // Method to style text fields
    private void styleTextField(JTextField textField) {
        textField.setBackground(new Color(255, 255, 255)); // White background for text field
        textField.setForeground(Color.BLACK); // Text color
        textField.setCaretColor(Color.BLACK); // Caret color
        textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200))); // Light gray border
    }

    // Method to style text areas
    private void styleTextArea(JTextArea textArea) {
        textArea.setBackground(new Color(255, 255, 255)); // White background for text area
        textArea.setForeground(Color.BLACK); // Text color
        textArea.setCaretColor(Color.BLACK); // Caret color
        textArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200))); // Light gray border
    }

    // Method to style buttons
    private void styleButton(JButton button) {
        button.setBackground(new Color(100, 149, 237)); // Button background color
        button.setForeground(Color.WHITE); // Button text color
        button.setFocusPainted(false); // Remove focus border
        button.setBorderPainted(false); // Remove border
        button.setOpaque(true); // Ensure button is opaque
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Font style and size
        button.setPreferredSize(new Dimension(160, 40)); // Increase button size for better appearance
    }

    // Method to submit feedback to the database
    private void submitFeedback() {
        String customerId = customerIdField.getText();
        String roomNumber = roomNumberField.getText();
        String feedback = feedbackArea.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Feedback (customer_id, room_number, feedback) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(customerId));
            stmt.setInt(2, Integer.parseInt(roomNumber));
            stmt.setString(3, feedback);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Feedback submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error submitting feedback: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to view feedback details from the database
    private void viewFeedback() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT f.customer_id, c.name, f.room_number, f.feedback " +
                    "FROM Feedback f " +
                    "JOIN Customers c ON f.customer_id = c.customer_id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            displayArea.setText("Customer ID\tCustomer Name\tRoom Number\tFeedback\n");
            displayArea.append("---------------------------------------------------------\n");

            while (rs.next()) {
                int customerId = rs.getInt("customer_id");
                String customerName = rs.getString("name");
                int roomNumber = rs.getInt("room_number");
                String feedback = rs.getString("feedback");

                displayArea.append(customerId + "\t" + customerName + "\t" + roomNumber + "\t" + feedback + "\n");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error fetching feedback: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FeedbackManagerGUI::new);
    }
}
