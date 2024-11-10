import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeManager {

    // Method to add an employee to the database
    public static void addEmployee(String name, int age, String mobile, double salary, String startedWorkedOn) {
        String query = "INSERT INTO employees (name, age, mobile, salary, started_worked_on) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, mobile);
            pstmt.setDouble(4, salary);
            pstmt.setDate(5, java.sql.Date.valueOf(startedWorkedOn)); // Converting string to SQL Date

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Employee added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all employees from the database
    public static String getAllEmployees() {
        StringBuilder employees = new StringBuilder();
        String query = "SELECT * FROM employees";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String mobile = rs.getString("mobile");
                double salary = rs.getDouble("salary");
                String startedWorkedOn = rs.getDate("started_worked_on").toString();

                employees.append("ID: ").append(id)
                        .append(", Name: ").append(name)
                        .append(", Age: ").append(age)
                        .append(", Mobile: ").append(mobile)
                        .append(", Salary: ").append(salary)
                        .append(", Started Worked On: ").append(startedWorkedOn)
                        .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees.toString();
    }

    // Method to edit an employee
    public static void editEmployee(int id, String name, int age, String mobile, double salary, String startedWorkedOn) {
        String query = "UPDATE employees SET name = ?, age = ?, mobile = ?, salary = ?, started_worked_on = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, mobile);
            pstmt.setDouble(4, salary);
            pstmt.setDate(5, java.sql.Date.valueOf(startedWorkedOn));
            pstmt.setInt(6, id);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Employee updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete an employee
    public static void deleteEmployee(int id) {
        String query = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Employee deleted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get a specific employee by ID
    public static String getEmployeeById(int id) {
        String employeeInfo = "";
        String query = "SELECT * FROM employees WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String mobile = rs.getString("mobile");
                    double salary = rs.getDouble("salary");
                    String startedWorkedOn = rs.getDate("started_worked_on").toString();

                    employeeInfo = "ID: " + id +
                            ", Name: " + name +
                            ", Age: " + age +
                            ", Mobile: " + mobile +
                            ", Salary: " + salary +
                            ", Started Worked On: " + startedWorkedOn;
                } else {
                    employeeInfo = "Employee not found!";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeInfo;
    }
}
