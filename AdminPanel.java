package WallStreet;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.ArrayList;
import java.util.List;

public class AdminPanel { //link to User authentification
    private List<User> users;
    private TradingEngine tradingEngine;
    private PortFolio portfolio;
    private Database database = new Database();

    public AdminPanel() {
        users = new ArrayList<>();
    }

    public void userList() {
        users = database.retriveUserList();
        List<User> userList = database.retriveUserList();

        List<User> usersToRemove = new ArrayList<>();

        for (User user : userList) {
            if (user.getName().equalsIgnoreCase("Admin")) {
                usersToRemove.add(user);
            }
        }

        userList.removeAll(usersToRemove);
        System.out.println("User list");
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.printf("%-20s %-20s %-20s %-20s \n", " Name", "Email", "Accountbalance", "Status");
        System.out.println("--------------------------------------------------------------------------------------------");
        if (users.isEmpty()) {
            System.out.printf("%-20s %-30s %-20s %-20s\n", "null", "null", "null", "null");
        }
        for (User user : userList) {
            System.out.printf("%-20s %-20s %-20s %-20s\n", user.getName(), user.getEmail(), user.getAccountBalance(), user.getStatus());
        }

    }


    public void disqualifyUser(String username) {
        users = database.retriveUserList();
        for (User user : users) {
            if (user.getName().equals(username)) {
                user.Disqualified();
                break;
            }
        }
    }


    public void checkAccountBalance() {
        users = database.retriveUserList();
        for (User user : users) {
            if (user.getStatus() != "disqualified") {
                portfolio = new PortFolio(user);
                double accountBalance = portfolio.getAccountBalance();
                if (accountBalance >= 0.5 * 500000 && accountBalance < 0) {
                    disqualifyUser(user.getName());
                }
            }
        }
    }





}
