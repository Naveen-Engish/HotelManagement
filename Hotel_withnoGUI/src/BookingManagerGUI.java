import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class BookingManagerGUI {
    private JFrame frame;
    private JTextField roomIdField, customerIdField, checkInField, checkOutField, totalPriceField;
    private JTable bookingTable;
    private DefaultTableModel tableModel;

    public BookingManagerGUI() {
        frame = new JFrame("Booking Manager");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 500); // Adjusted size for better layout
        frame.setLayout(new BorderLayout());

        // Set off-white background color
        frame.getContentPane().setBackground(new Color(245, 245, 245)); // Off-white

        // Input fields for booking data
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10)); // Added spacing between components
        inputPanel.setBackground(new Color(245, 245, 245)); // Off-white

        JLabel roomIdLabel = new JLabel("Room ID:");
        roomIdLabel.setForeground(Color.BLACK);
        roomIdField = new JTextField();
        roomIdField.setBackground(Color.LIGHT_GRAY);

        JLabel customerIdLabel = new JLabel("Customer ID:");
        customerIdLabel.setForeground(Color.BLACK);
        customerIdField = new JTextField();
        customerIdField.setBackground(Color.LIGHT_GRAY);

        JLabel checkInLabel = new JLabel("Check-In Date (YYYY-MM-DD):");
        checkInLabel.setForeground(Color.BLACK);
        checkInField = new JTextField();
        checkInField.setBackground(Color.LIGHT_GRAY);

        JLabel checkOutLabel = new JLabel("Check-Out Date (YYYY-MM-DD):");
        checkOutLabel.setForeground(Color.BLACK);
        checkOutField = new JTextField();
        checkOutField.setBackground(Color.LIGHT_GRAY);

        JLabel totalPriceLabel = new JLabel("Total Price:");
        totalPriceLabel.setForeground(Color.BLACK);
        totalPriceField = new JTextField();
        totalPriceField.setBackground(Color.LIGHT_GRAY);

        inputPanel.add(roomIdLabel);
        inputPanel.add(roomIdField);
        inputPanel.add(customerIdLabel);
        inputPanel.add(customerIdField);
        inputPanel.add(checkInLabel);
        inputPanel.add(checkInField);
        inputPanel.add(checkOutLabel);
        inputPanel.add(checkOutField);
        inputPanel.add(totalPriceLabel);
        inputPanel.add(totalPriceField);

        // Buttons for actions
        JButton addBookingButton = new JButton("Add Booking");
        addBookingButton.setForeground(Color.BLACK);
        addBookingButton.setBackground(new Color(173, 216, 230)); // Light blue

        JButton viewBookingsButton = new JButton("View Bookings");
        viewBookingsButton.setForeground(Color.BLACK);
        viewBookingsButton.setBackground(new Color(173, 216, 230)); // Light blue

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245)); // Off-white
        buttonPanel.add(addBookingButton);
        buttonPanel.add(viewBookingsButton);

        // Table for displaying booking data
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Booking ID");
        tableModel.addColumn("Room ID");
        tableModel.addColumn("Customer ID");
        tableModel.addColumn("Check-In Date");
        tableModel.addColumn("Check-Out Date");
        tableModel.addColumn("Total Price");

        bookingTable = new JTable(tableModel);
        bookingTable.setPreferredScrollableViewportSize(new Dimension(550, 200)); // Resize table for better display
        bookingTable.setFillsViewportHeight(true);
        bookingTable.setBackground(Color.LIGHT_GRAY);
        bookingTable.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(bookingTable);

        // Add components to the frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        frame.setVisible(true);

        // Action listeners for buttons
        addBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBookingToDatabase();
            }
        });

        viewBookingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayBookings();
            }
        });
    }

    // Method to add a booking to the database
    private void addBookingToDatabase() {
        String roomId = roomIdField.getText();
        String customerId = customerIdField.getText();
        String checkInDate = checkInField.getText();
        String checkOutDate = checkOutField.getText();
        String totalPrice = totalPriceField.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Bookings (room_id, customer_id, check_in_date, check_out_date, total_price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomId);
            pstmt.setString(2, customerId);
            pstmt.setString(3, checkInDate);
            pstmt.setString(4, checkOutDate);
            pstmt.setString(5, totalPrice);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Booking added successfully!");
                clearFields();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error adding booking: " + ex.getMessage());
        }
    }

    // Method to display all bookings in the table
    private void displayBookings() {
        // Clear previous table data
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Bookings";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            // Add booking data to the table
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("booking_id"),
                        rs.getInt("room_id"),
                        rs.getInt("customer_id"),
                        rs.getString("check_in_date"),
                        rs.getString("check_out_date"),
                        rs.getDouble("total_price")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error retrieving bookings: " + ex.getMessage());
        }
    }

    // Clear input fields after adding a booking
    private void clearFields() {
        roomIdField.setText("");
        customerIdField.setText("");
        checkInField.setText("");
        checkOutField.setText("");
        totalPriceField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookingManagerGUI());
    }
}
