import java.sql.*;
import java.util.*;

public class SMR {

    private static final String URL  = "jdbc:mysql://localhost:3306/meddb";
    private static final String USER = "root";
    private static final String PASS = "rushi@2004";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Smart Medicine Reminder ---");
            System.out.println("1. Add Reminder");
            System.out.println("2. View All Reminders");
            System.out.println("3. Delete Reminder");
            System.out.println("4. Exit");
            System.out.print("Choose: ");
            int choice = Integer.parseInt(sc.nextLine().trim());

            switch (choice) {
                case 1 -> addReminder(sc);
                case 2 -> viewReminders();
                case 3 -> deleteReminder(sc);
                case 4 -> { System.out.println("Goodbye!"); sc.close(); return; }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void addReminder(Scanner sc) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            System.out.print("Patient name: ");
            String patient = sc.nextLine();
            System.out.print("Medicine name: ");
            String med = sc.nextLine();
            System.out.print("Dose (e.g. 500mg): ");
            String dose = sc.nextLine();
            System.out.print("Doctor name: ");
            String doctor = sc.nextLine();
            System.out.print("Special instructions: ");
            String instr = sc.nextLine();
            System.out.print("Reminder time (yyyy-mm-dd hh:mm:ss): ");
            String time = sc.nextLine();

            String sql = "INSERT INTO reminder(patient_name,medicine_name,dose,doctor_name,instructions,reminder_time)"
                       + " VALUES (?,?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, patient);
                ps.setString(2, med);
                ps.setString(3, dose);
                ps.setString(4, doctor);
                ps.setString(5, instr);
                ps.setString(6, time);
                ps.executeUpdate();
                System.out.println("Reminder added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewReminders() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM reminder ORDER BY reminder_time")) {

            System.out.println("\n--- All Reminders ---");
            while (rs.next()) {
                System.out.printf("%d | %s | %s | %s | %s | %s | %s%n",
                        rs.getInt("id"),
                        rs.getString("patient_name"),
                        rs.getString("medicine_name"),
                        rs.getString("dose"),
                        rs.getString("doctor_name"),
                        rs.getString("instructions"),
                        rs.getTimestamp("reminder_time"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteReminder(Scanner sc) {
        System.out.print("Enter reminder ID to delete: ");
        int id = Integer.parseInt(sc.nextLine());
        String sql = "DELETE FROM reminder WHERE id=?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Reminder deleted!");
            else System.out.println("No reminder found with that ID.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
