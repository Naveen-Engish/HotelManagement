import java.sql.*;
import java.util.Scanner;

public class BookingManager {
    public static void addBooking() {
        try (Connection conn = DatabaseConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Enter customer ID:");
            int customerId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.println("Enter room ID:");
            int roomId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.println("Enter booking date (YYYY-MM-DD):");
            String bookingDate = scanner.nextLine();

            String sql = "INSERT INTO Bookings (customer_id, room_id, booking_date) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, customerId);
            stmt.setInt(2, roomId);
            stmt.setString(3, bookingDate);
            stmt.executeUpdate();
            System.out.println("Booking added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewBookings() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT b.*, c.name AS customer_name, r.room_number FROM Bookings b " +
                    "JOIN Customers c ON b.customer_id = c.customer_id " +
                    "JOIN Rooms r ON b.room_id = r.room_id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println("Booking ID: " + rs.getInt("booking_id") +
                        ", Customer: " + rs.getString("customer_name") +
                        ", Room Number: " + rs.getInt("room_number") +
                        ", Booking Date: " + rs.getString("booking_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editBooking() {
        try (Connection conn = DatabaseConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Enter Booking ID to edit:");
            int bookingId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.println("Enter new customer ID:");
            int customerId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.println("Enter new room ID:");
            int roomId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.println("Enter new booking date (YYYY-MM-DD):");
            String bookingDate = scanner.nextLine();

            String sql = "UPDATE Bookings SET customer_id = ?, room_id = ?, booking_date = ? WHERE booking_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, customerId);
            stmt.setInt(2, roomId);
            stmt.setString(3, bookingDate);
            stmt.setInt(4, bookingId);
            stmt.executeUpdate();
            System.out.println("Booking updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteBooking() {
        try (Connection conn = DatabaseConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Enter Booking ID to delete:");
            int bookingId = scanner.nextInt();

            String sql = "DELETE FROM Bookings WHERE booking_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookingId);
            stmt.executeUpdate();
            System.out.println("Booking deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
