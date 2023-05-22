/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dsgroup;

/**
 *
 * @author sharr
 */
import java.util.ArrayList;
import java.util.List;

public class TestReport {

    public static void main(String[] args) {
        // Create sample users
        User user1 = new User("101", 7000.0, 1500.0, 60, 40, 20);
        User user2 = new User("102", 4000.0, -1000.0, 30, 15, 15);
        User user3 = new User("103", 12000.0, 2500.0, 100, 80, 20);

        // Generate report for a single user in different formats
        ReportGenerator.generateReport(user1, "txt");
        ReportGenerator.generateReport(user2, "csv");
        ReportGenerator.generateReport(user3, "pdf");

        // Generate report for all users in different formats
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        ReportGenerator.generateReport(userList, "txt");
        ReportGenerator.generateReport(userList, "csv");
        ReportGenerator.generateReport(userList, "pdf");

        // Print a success message
        System.out.println("Reports generated successfully.");
    }
}

