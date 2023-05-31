/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package wallstreetwarriors;

import java.util.Scanner;
import java.io.*;

/**
 *
 * @author Joanne Lim Zi Xuan 22004882/1
 */
public class CreateAccount {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 1. Create a trading account
        System.out.println("Welcome to the trading competition!");
        System.out.println("Please enter your details to create a trading account.");
        
        System.out.print("Enter your full name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter your email address: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter a password(Must be 8 characters including alphabets, numbers and special characters: ");
        String password = scanner.nextLine();
        
        // Validate the password
        while (!isValidPassword(password)) {
            System.out.println("Password is invalid!");
            System.out.print("Enter a password: ");
            password = scanner.nextLine();
        }
        System.out.println("Password is valid!");
        // Assuming the brokerage firm has a method called "createAccount" to create a trading account
        TradingAccount account = createAccount(name, email, password);
        String fileName = "list of accounts.txt";
        String[] lines = {name, email, password};
        try(PrintWriter outputStream = new PrintWriter(new FileOutputStream("C:\\Users\\Admin\\OneDrive\\Documents\\NetBeansProjects\\WallStreetWarriors\\list of accounts.txt",true))){
            outputStream.println();
            for(String i : lines){
                outputStream.print(i + " ");
            }
            outputStream.close();
            System.out.println("Data written to " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing to " + fileName + ": " + e.getMessage());
        }
        
            System.out.println("Congratulations! Your trading account has been created successfully.");
            
        scanner.close();
    
    }
    
    public static TradingAccount createAccount(String name, String email, String password) {
        TradingAccount account = new TradingAccount(name, email, password);
        // Code to create the account with the brokerage firm can be added here
        return account;
    }
    
    public static boolean isValidPassword(String password) {
        // Check if the password is exactly 8 characters long
        if (password.length() != 8) {
            return false;
        }
        
        // Check if the password contains at least one alphabet, one number, and one special character
        boolean hasAlphabet = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasAlphabet = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else {
                hasSpecialChar = true;
            }
        }
        
        if (!hasAlphabet || !hasNumber || !hasSpecialChar) {
            return false;
        }
        
        return true;
    }
    
}

class TradingAccount {
    
    private String name;
    private String email;
    private String password;
    
    public TradingAccount(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setName(String newName) {
        name = newName;
    }
    
    public void setEmail(String newEmail) {
        email = newEmail;
    }
    
    public void setPassword(String newPassword) {
        password = newPassword;
    }
    
}