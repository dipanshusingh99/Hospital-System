package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Doctor {
    private Connection connection;
    private Scanner scanner;

    public Doctor(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    // Add Doctor with validation
    public void addDoctor(){
        String name;
        while(true){
            System.out.print("Enter Doctor Name: ");
            name = scanner.next();
            if(name.matches("[a-zA-Z]+")) break;
            else System.out.println("Invalid name! Alphabets only.");
        }

        String specialization;
        while(true){
            System.out.print("Enter Specialization: ");
            specialization = scanner.next();
            if(specialization.matches("[a-zA-Z]+")) break;
            else System.out.println("Invalid input! Alphabets only.");
        }

        try{
            String query = "INSERT INTO doctors(name, specialization) VALUES(?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, specialization);
            int rows = ps.executeUpdate();
            if(rows > 0) System.out.println("Doctor Added Successfully!");
            else System.out.println("Failed to add Doctor!");
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void viewDoctors(){
        String query = "SELECT * FROM doctors";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            System.out.println("+------------+--------------------+------------------+");
            System.out.println("| Doctor Id  | Name               | Specialization   |");
            System.out.println("+------------+--------------------+------------------+");
            while(rs.next()){
                System.out.printf("| %-10d | %-18s | %-16s |\n",
                        rs.getInt("id"), rs.getString("name"), rs.getString("specialization"));
                System.out.println("+------------+--------------------+------------------+");
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int id){
        String query = "SELECT * FROM doctors WHERE id = ?";
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