package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManager {

    private static final String url = "jdbc:mysql://127.0.0.1:3306/HospitalSystem";
    private static final String username = "root";
    private static final String password = "root";

    public static void main(String[] args) {

        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch(ClassNotFoundException e) { e.printStackTrace(); }

        Scanner scanner = new Scanner(System.in);

        try(Connection connection = DriverManager.getConnection(url, username, password)) {

            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection, scanner);

            while(true){
                System.out.println("\nHOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. Add Doctor");
                System.out.println("4. View Doctors");
                System.out.println("5. Book Appointment");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");

                String choiceInput = scanner.next();
                if(!choiceInput.matches("\\d+")){
                    System.out.println("Invalid input! Enter numbers only.");
                    continue;
                }
                int choice = Integer.parseInt(choiceInput);

                switch(choice){
                    case 1: patient.addPatient(); break;
                    case 2: patient.viewPatients(); break;
                    case 3: doctor.addDoctor(); break;
                    case 4: doctor.viewDoctors(); break;
                    case 5: bookAppointment(patient, doctor, connection, scanner); break;
                    case 6:
                        System.out.println("THANK YOU FOR USING HOSPITAL SYSTEM!");
                        return;
                    default:
                        System.out.println("Enter valid choice!");
                }
            }

        } catch(SQLException e){ e.printStackTrace(); }
    }

    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner){

        int patientId;
        int doctorId;
        String date;

        while(true){
            System.out.print("Enter Patient Id: ");
            String input = scanner.next();
            if(input.matches("\\d+")) { patientId = Integer.parseInt(input); break; }
            else System.out.println("Invalid Patient Id!");
        }

        while(true){
            System.out.print("Enter Doctor Id: ");
            String input = scanner.next();
            if(input.matches("\\d+")) { doctorId = Integer.parseInt(input); break; }
            else System.out.println("Invalid Doctor Id!");
        }

        while(true){
            System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
            date = scanner.next();
            if(date.matches("\\d{4}-\\d{2}-\\d{2}")) break;
            else System.out.println("Invalid Date format!");
        }

        if(!patient.getPatientById(patientId) || !doctor.getDoctorById(doctorId)){
            System.out.println("Either Doctor or Patient does not exist!");
            return;
        }

        if(!checkDoctorAvailability(doctorId, date, connection)){
            System.out.println("Doctor not available on this date!");
            return;
        }

        try{
            String query = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, patientId);
            ps.setInt(2, doctorId);
            ps.setString(3, date);
            int rows = ps.executeUpdate();
            if(rows>0) System.out.println("Appointment Booked!");
            else System.out.println("Failed to book appointment!");
        } catch(SQLException e){ e.printStackTrace(); }
    }

    public static boolean checkDoctorAvailability(int doctorId, String date, Connection connection){
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id=? AND appointment_date=?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, doctorId);
            ps.setString(2, date);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){ return rs.getInt(1)==0; }
        } catch(SQLException e){ e.printStackTrace(); }
        return false;
    }
}