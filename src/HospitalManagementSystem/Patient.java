package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    // Add Patient with full validation
    public void addPatient(){

        String name;
        while(true){
            System.out.print("Enter Patient Name: ");
            name = scanner.next();
            if(name.matches("[a-zA-Z]+")){
                break;
            } else {
                System.out.println("Invalid name! Only alphabets allowed.");
            }
        }

        int age;
        while(true){
            System.out.print("Enter Patient Age: ");
            String ageInput = scanner.next();
            if(ageInput.matches("\\d+")){
                age = Integer.parseInt(ageInput);
                if(age > 0) break;
                else System.out.println("Age must be greater than 0.");
            } else {
                System.out.println("Invalid age! Numbers only.");
            }
        }

        String gender;
        while(true){
            System.out.print("Enter Patient Gender (male/female/others): ");
            gender = scanner.next().toLowerCase();
            if(gender.equals("male") || gender.equals("female") || gender.equals("others")){
                break;
            } else {
                System.out.println("Invalid gender! Only male, female, or others.");
            }
        }

        try{
            String query = "INSERT INTO patients(name, age, gender) VALUES(?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            int rows = ps.executeUpdate();
            if(rows > 0) System.out.println("Patient Added Successfully!");
            else System.out.println("Failed to add Patient!");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void viewPatients(){
        String query = "SELECT * FROM patients";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            System.out.println("+------------+--------------------+----------+------------+");
            System.out.println("| Patient Id | Name               | Age      | Gender     |");
            System.out.println("+------------+--------------------+----------+------------+");
            while(rs.next()){
                System.out.printf("| %-10d | %-18s | %-8d | %-10s |\n",
                        rs.getInt("id"), rs.getString("name"),
                        rs.getInt("age"), rs.getString("gender"));
                System.out.println("+------------+--------------------+----------+------------+");
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id){
        String query = "SELECT * FROM patients WHERE id = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}