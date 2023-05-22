/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package wallstreetwarriors;

import java.util.Scanner;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author Admin
 */
public class LogIn extends CreateAccount{

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 2. Log in to the app
            System.out.println("Please enter your account credentials to log in to the trading competition app.");
        
            System.out.print("Enter your email address: ");
            String loginEmail = scanner.nextLine();
        
            System.out.print("Enter your password: ");
            String loginPassword = scanner.nextLine();
        String filePath = "C:/Users/Admin/OneDrive/Documents/NetBeansProjects/WallStreetWarriors/list of accounts.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Pattern pattern = Pattern.compile("\\b" + loginEmail + " " + loginPassword + "\\b"); // create a pattern for the target word
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    System.out.println("Logged in successfully!");
                // Code for the trading competition app can be added here
                } else {
                    System.out.println("Incorrect email address or password. Please try again.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        scanner.close();
    }
    
}
