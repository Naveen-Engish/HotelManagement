import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EmployeeManagerGUI {
    public EmployeeManagerGUI() {
        JFrame frame = new JFrame("Employee Manager");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.getContentPane().setBackground(new Color(245, 245, 245)); // Off-white background

        // Create buttons with styles
        JButton addEmployeeButton = createStyledButton("Add Employee");
        JButton viewEmployeesButton = createStyledButton("View Employees");

        // Action listeners for buttons
        addEmployeeButton.addActionListener(e -> addEmployee());
        viewEmployeesButton.addActionListener(e -> viewEmployees());

        // Create panel and add buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));  // Organize buttons vertically
        panel.setBackground(new Color(245, 245, 245)); // Panel off-white background
        panel.add(addEmployeeButton);
        panel.add(viewEmployeesButton);

        // Add panel to frame and make it visible
        frame.add(panel);
        frame.setVisible(true);
    }

    // Method to create a styled button with hover effect
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(100, 149, 237)); // Button background color
        button.setForeground(Color.WHITE); // Button text color
        button.setFocusPainted(false); // Remove focus border
        button.setBorderPainted(false); // Remove border
        button.setOpaque(true); // Ensure button is opaque
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Font style and size

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            Color originalColor = button.getBackground();
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(65, 105, 225)); // Darker blue on hover
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor); // Revert to original color
            }
        });
        return button;
    }

    private void addEmployee() {
        JFrame addFrame = new JFrame("Add Employee");
        addFrame.setSize(400, 300);
        addFrame.getContentPane().setBackground(new Color(245, 245, 245)); // Off-white background

        // Fields for employee details
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.DARK_GRAY);
        JTextField nameField = createStyledTextField();

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setForeground(Color.DARK_GRAY);
        JTextField ageField = createStyledTextField();

        JLabel mobileLabel = new JLabel("Mobile:");
        mobileLabel.setForeground(Color.DARK_GRAY);
        JTextField mobileField = createStyledTextField();

        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setForeground(Color.DARK_GRAY);
        JTextField salaryField = createStyledTextField();

        JLabel startedOnLabel = new JLabel("Started Worked On:");
        startedOnLabel.setForeground(Color.DARK_GRAY);
        JTextField startedOnField = createStyledTextField();  // Should be formatted as "YYYY-MM-DD"

        // Button to submit employee details
        JButton submitButton = createStyledButton("Submit");

        // Layout for form
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));
        panel.setBackground(new Color(245, 245, 245)); // Off-white background for form panel
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(ageLabel);
        panel.add(ageField);
        panel.add(mobileLabel);
        panel.add(mobileField);
        panel.add(salaryLabel);
        panel.add(salaryField);
        panel.add(startedOnLabel);
        panel.add(startedOnField);
        panel.add(new JLabel());  // Empty placeholder
        panel.add(submitButton);

        addFrame.add(panel);
        addFrame.setVisible(true);

        // Action listener for submit button
        submitButton.addActionListener(e -> {
            // Logic to store employee in the database
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String mobile = mobileField.getText();
            double salary = Double.parseDouble(salaryField.getText());
            String startedOn = startedOnField.getText();

            // Call the method to insert employee into the database
            EmployeeManager.addEmployee(name, age, mobile, salary, startedOn);
            JOptionPane.showMessageDialog(addFrame, "Employee Added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            addFrame.dispose();
        });
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(new Color(224, 240, 255)); // Light blue background
        textField.setForeground(Color.DARK_GRAY); // Dark gray text color
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        return textField;
    }

    private void viewEmployees() {
        JFrame viewFrame = new JFrame("Employee List");
        viewFrame.setSize(500, 400);
        viewFrame.getContentPane().setBackground(new Color(245, 245, 245)); // Off-white background

        JTextArea employeeDisplay = new JTextArea();
        employeeDisplay.setEditable(false);
        employeeDisplay.setBackground(new Color(224, 240, 255)); // Light blue text area background
        employeeDisplay.setForeground(Color.DARK_GRAY); // Text color
        employeeDisplay.setCaretColor(Color.DARK_GRAY); // Caret color

        // Retrieve and format employee data
        String employees = EmployeeManager.getAllEmployees(); // Placeholder method

        // Add header for a neater display
        String header = String.format("%-10s %-15s %-15s %-10s %-15s\n",
                "ID", "Name", "Age", "Mobile", "Salary", "Started On");
        String separator = "---------------------------------------------------------------\n";
        employeeDisplay.append(header + separator + employees);

        viewFrame.add(new JScrollPane(employeeDisplay));
        viewFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeManagerGUI::new);
    }
}
