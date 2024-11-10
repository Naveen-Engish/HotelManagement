import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;

public class HotelManagementSystemGUI {
    private JFrame frame;
    private JPanel buttonPanel;
    private JButton manageRoomsButton, manageCustomersButton, manageBookingsButton, manageFeedbackButton, manageEmployeesButton, exitButton;
    private JPanel titleBar;
    private JLabel titleLabel;
    private int xMouse, yMouse;
    private Font customFont;

    public HotelManagementSystemGUI() {
        try {
            // Load custom font
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("C:/Users/USER/IdeaProjects/Hotel_withnoGUI/font/PatrickHandSC-Regular.ttf")).deriveFont(18f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            customFont = new Font("SansSerif", Font.BOLD, 30); // Fallback font
        }

        // Frame Setup
        frame = new JFrame("Hotel Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 450);
        frame.setUndecorated(true);
        frame.setOpacity(0.95f);
        frame.setShape(new RoundRectangle2D.Double(0, 0, 700, 450, 20, 20));

        // Title Bar with Gradient and Shadow
        titleBar = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(58, 83, 155), getWidth(), 0, new Color(101, 148, 220));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        titleBar.setLayout(new BorderLayout());
        titleBar.setPreferredSize(new Dimension(700, 40));

        titleLabel = new JLabel("Hotel Management System", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(customFont); // Apply custom font
        titleBar.add(titleLabel, BorderLayout.CENTER);

        // Title Bar Buttons (Minimize and Close)
        JButton minimizeButton = createTitleBarButton("-");
        JButton closeButton = createTitleBarButton("X");
        minimizeButton.addActionListener(e -> frame.setState(JFrame.ICONIFIED));
        closeButton.addActionListener(e -> System.exit(0));

        JPanel buttonPanelTitle = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanelTitle.setOpaque(false);
        buttonPanelTitle.add(minimizeButton);
        buttonPanelTitle.add(closeButton);
        titleBar.add(buttonPanelTitle, BorderLayout.EAST);

        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                xMouse = e.getX();
                yMouse = e.getY();
            }
        });
        titleBar.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                frame.setLocation(frame.getX() + e.getX() - xMouse, frame.getY() + e.getY() - yMouse);
            }
        });

        // Button Panel with Modern Aesthetic
        buttonPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        buttonPanel.setBackground(new Color(245, 245, 245));

        manageRoomsButton = createButton("Manage Rooms");
        manageCustomersButton = createButton("Manage Customers");
        manageBookingsButton = createButton("Manage Bookings");
        manageFeedbackButton = createButton("Manage Feedback");
        manageEmployeesButton = createButton("Manage Employees");
        exitButton = createButton("Exit");

        buttonPanel.add(manageRoomsButton);
        buttonPanel.add(manageCustomersButton);
        buttonPanel.add(manageBookingsButton);
        buttonPanel.add(manageFeedbackButton);
        buttonPanel.add(manageEmployeesButton);
        buttonPanel.add(exitButton);

        // Right-side Image Panel
        JPanel imagePanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("path/to/your/image.jpg"); // Load an image here
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        imagePanel.setPreferredSize(new Dimension(400, 400));

        // Split Panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buttonPanel, imagePanel);
        splitPane.setDividerLocation(300);
        splitPane.setDividerSize(0);
        splitPane.setEnabled(false);

        frame.setLayout(new BorderLayout());
        frame.add(titleBar, BorderLayout.NORTH);
        frame.add(splitPane, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);

        // Show Login Dialog
        showLoginDialog();

        manageRoomsButton.addActionListener(e -> new RoomManagerGUI());
        manageCustomersButton.addActionListener(e -> new CustomerManagerGUI());
        manageBookingsButton.addActionListener(e -> new BookingManagerGUI());
        manageFeedbackButton.addActionListener(e -> new FeedbackManagerGUI());
        manageEmployeesButton.addActionListener(e -> new EmployeeManagerGUI());
        exitButton.addActionListener(e -> {
            int confirmation = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        frame.setVisible(true);
    }

    private void showLoginDialog() {
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);

        int option = JOptionPane.showConfirmDialog(frame, loginPanel, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (!username.equals("admin") || !password.equals("admin")) {
                JOptionPane.showMessageDialog(frame, "Invalid credentials, please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                showLoginDialog();
            }
        } else {
            System.exit(0);
        }
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.WHITE);
        button.setFont(customFont.deriveFont(30f)); // Apply custom font
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(200, 50));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(65, 105, 225));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(100, 149, 237));
            }
        });

        return button;
    }

    private JButton createTitleBarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(customFont.deriveFont(20f)); // Apply custom font
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(30, 30, 30));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(30, 30));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HotelManagementSystemGUI::new);
    }
}
