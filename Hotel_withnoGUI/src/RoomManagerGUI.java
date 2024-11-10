import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;
import java.io.IOException;
import java.awt.font.*;

public class RoomManagerGUI {
    private JFrame frame;
    private JTextField roomNumberField, roomTypeField, priceField;
    private JTextArea displayArea;
    private Font customFont;

    public RoomManagerGUI() {
        // Load custom font
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("C:/Users/USER/IdeaProjects/Hotel_withnoGUI/font/PatrickHandSC-Regular.ttf")).deriveFont(14f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // Frame setup
        frame = new JFrame("Room Manager");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 500);
        frame.getContentPane().setBackground(new Color(245, 245, 245)); // Off-white background
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Input fields for room data
        JLabel roomNumberLabel = createStyledLabel("Room Number:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        frame.add(roomNumberLabel, gbc);

        roomNumberField = createStyledTextField();
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        frame.add(roomNumberField, gbc);

        JLabel roomTypeLabel = createStyledLabel("Room Type:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        frame.add(roomTypeLabel, gbc);

        roomTypeField = createStyledTextField();
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        frame.add(roomTypeField, gbc);

        JLabel priceLabel = createStyledLabel("Price per Night:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        frame.add(priceLabel, gbc);

        priceField = createStyledTextField();
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        frame.add(priceField, gbc);

        // Buttons for adding and viewing rooms
        JButton addRoomButton = createStyledButton("Add Room");
        JButton viewRoomsButton = createStyledButton("View Rooms");

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2;
        frame.add(addRoomButton, gbc);

        gbc.gridy = 4;
        frame.add(viewRoomsButton, gbc);

        // Display area with scroll pane
        displayArea = new JTextArea(10, 40);
        displayArea.setEditable(false);
        displayArea.setBackground(new Color(230, 245, 255)); // Very light blue for display area
        displayArea.setForeground(Color.BLACK); // Black text color
        displayArea.setCaretColor(Color.BLACK);
        displayArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(displayArea);
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        frame.add(scrollPane, gbc);

        frame.setVisible(true);

        // Action listeners
        addRoomButton.addActionListener(e -> addRoomToDatabase());
        viewRoomsButton.addActionListener(e -> displayRooms());
    }

    // Method to create a styled label
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.BLACK); // Black text color for label
        label.setFont(customFont); // Apply custom font
        return label;
    }

    // Method to create a styled button
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(100, 149, 237)); // Cornflower blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(customFont); // Apply custom font
        button.setPreferredSize(new Dimension(120, 35));
        return button;
    }

    // Method to create a styled text field with very light blue color
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(15);
        textField.setBackground(new Color(173, 216, 230)); // Very light blue
        textField.setForeground(Color.BLACK); // Black text color
        textField.setCaretColor(Color.BLACK);
        textField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textField.setFont(customFont); // Apply custom font
        return textField;
    }

    // Method to add room data to the database
    private void addRoomToDatabase() {
        String roomNumber = roomNumberField.getText();
        String roomType = roomTypeField.getText();
        String price = priceField.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Rooms (room_number, room_type, price_per_night, is_available) VALUES (?, ?, ?, TRUE)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomNumber);
            pstmt.setString(2, roomType);
            pstmt.setBigDecimal(3, new java.math.BigDecimal(price));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Room added successfully!");
                clearFields();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error adding room: " + ex.getMessage());
        }
    }

    // Method to display rooms in the GUI
    // Method to display rooms in a more elegant format
    private void displayRooms() {
        displayArea.setText(""); // Clear previous data

        // Adding header with column names
        String header = String.format("%-10s %-15s %-15s %-15s %-10s\n",
                "Room ID", "Room Number", "Room Type", "Price per Night", "Available");
        String separator = "------------------------------------------------------------\n";
        displayArea.append(header + separator);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Rooms";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            // Format each row's data for aligned columns
            while (rs.next()) {
                String roomInfo = String.format("%-10d %-15s %-15s %-15.2f %-10s\n",
                        rs.getInt("room_id"),
                        rs.getString("room_number"),
                        rs.getString("room_type"),
                        rs.getBigDecimal("price_per_night"),
                        rs.getBoolean("is_available") ? "Yes" : "No");
                displayArea.append(roomInfo);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error retrieving rooms: " + ex.getMessage());
        }
    }


    // Clear input fields after adding a room
    private void clearFields() {
        roomNumberField.setText("");
        roomTypeField.setText("");
        priceField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RoomManagerGUI::new);
    }
}
