import java.sql.*;
import java.util.Scanner;

public class CustomerManager {
    public static void addCustomer() {
        try (Connection conn = DatabaseConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Enter customer name:");
            String name = scanner.nextLine();

            System.out.println("Enter phone number:");
            String phone = scanner.nextLine();

            System.out.println("Enter email:");
            String email = scanner.nextLine();

            System.out.println("Enter address:");
            String address = scanner.nextLine();

            String sql = "INSERT INTO Customers (name, phone_number, email, address) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, email);
            stmt.setString(4, address);
            stmt.executeUpdate();
            System.out.println("Customer added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewCustomers() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Customers";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println("Customer ID: " + rs.getInt("customer_id") +
                        ", Name: " + rs.getString("name") +
                        ", Phone: " + rs.getString("phone_number") +
                        ", Email: " + rs.getString("email") +
                        ", Address: " + rs.getString("address"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editCustomer() {
        try (Connection conn = DatabaseConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Enter Customer ID to edit:");
            int customerId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.println("Enter new customer name:");
            String name = scanner.nextLine();

            System.out.println("Enter new phone number:");
            String phone = scanner.nextLine();

            System.out.println("Enter new email:");
            String email = scanner.nextLine();

            System.out.println("Enter new address:");
            String address = scanner.nextLine();

            String sql = "UPDATE Customers SET name = ?, phone_number = ?, email = ?, address = ? WHERE customer_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, email);
            stmt.setString(4, address);
            stmt.setInt(5, customerId);
            stmt.executeUpdate();
            System.out.println("Customer updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCustomer() {
        try (Connection conn = DatabaseConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Enter Customer ID to delete:");
            int customerId = scanner.nextInt();

            String sql = "DELETE FROM Customers WHERE customer_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
            System.out.println("Customer deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
