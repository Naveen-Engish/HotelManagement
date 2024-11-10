import java.sql.*;
import java.util.Scanner;

public class RoomManager {
    public static void addRoom() {
        try (Connection conn = DatabaseConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Enter room number:");
            int roomNumber = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.println("Enter room type (e.g., Single, Double, Suite):");
            String roomType = scanner.nextLine();

            System.out.println("Enter room price:");
            double price = scanner.nextDouble();

            String sql = "INSERT INTO Rooms (room_number, room_type,  price_per_night) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, roomNumber);
            stmt.setString(2, roomType);
            stmt.setDouble(3, price);
            stmt.executeUpdate();
            System.out.println("Room added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewRooms() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Rooms";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println("Room ID: " + rs.getInt("room_id") +
                        ", Room Number: " + rs.getInt("room_number") +
                        ", Room Type: " + rs.getString("room_type") +
                        ", Price: $" + rs.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editRoom() {
        try (Connection conn = DatabaseConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Enter Room ID to edit:");
            int roomId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.println("Enter new room number:");
            int roomNumber = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.println("Enter new room type:");
            String roomType = scanner.nextLine();

            System.out.println("Enter new price:");
            double price = scanner.nextDouble();

            String sql = "UPDATE Rooms SET room_number = ?, room_type = ?, price = ? WHERE room_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, roomNumber);
            stmt.setString(2, roomType);
            stmt.setDouble(3, price);
            stmt.setInt(4, roomId);
            stmt.executeUpdate();
            System.out.println("Room updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteRoom() {
        try (Connection conn = DatabaseConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Enter Room ID to delete:");
            int roomId = scanner.nextInt();

            String sql = "DELETE FROM Rooms WHERE room_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, roomId);
            stmt.executeUpdate();
            System.out.println("Room deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
