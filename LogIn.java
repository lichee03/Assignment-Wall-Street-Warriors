package wallstreetwarriors;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LogIn {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filePath = "C:\\Users\\Admin\\OneDrive\\Documents\\NetBeansProjects\\WallStreetWarriors\\list of accounts.txt";

        // Get the email and password from user input
        System.out.print("Enter the email: ");
        String email = scanner.nextLine();

        System.out.print("Enter the password: ");
        String password = scanner.nextLine();

        // Find matching email and password pairs in the file
        boolean isMatchFound = findMatchingEmailPasswordPairs(filePath, email, password);

        if (isMatchFound) {
            System.out.println("Logged in successfully!");
        } else {
            System.out.println("Incorrect email address or password. Please try again.janice@");
        }

        scanner.close();
    }

    public static boolean findMatchingEmailPasswordPairs(String filePath, String email, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(" ");

                if (credentials.length == 3) {
                    String storedEmail = credentials[1].trim();
                    String storedPassword = credentials[2].trim();

                    if (storedEmail.equals(email) && storedPassword.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }

        return false;
    }
}
}
