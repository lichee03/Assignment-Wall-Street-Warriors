/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lichee
 */
public class AdminPanel {
    private List<User> users;
    private TradingEngine tradingEngine;

    public AdminPanel() {
        users = new ArrayList<>();
    }
    public boolean CheckShortSell(Stock stock,PortFolio portfolio){
        boolean shortsell=true;
        int currentshares = portfolio.getHoldings().getOrDefault(stock, 0);
        if(!tradingEngine.tryExecuteBuyOrders(stock, portfolio)){
            
        }
    }
    public boolean CheckMarginTrade(){
        boolean marginTrade=true;
    }
    


    public void disqualifyUser(String username) {
        for (User user : users) {
            if (user.getName().equals(username)) {
                user.disqualify();
                break;
            }
        }
    }

    public void autoFraudDetection() {
        for (User user : users) {
            if (!user.isDisqualified()) {
                List<Order> pendingOrders = user.getPendingOrders();
                for (Order order : pendingOrders) {
                    // Check for short-selling or other fraudulent transactions
                    if (order.getQuantity() < 0) {
                        System.out.println("Fraud detected: Short-selling by user " + user.getUsername() + " - Order: " + order.getSymbol() + " - " + order.getQuantity() + " shares at " + order.getPrice());
                    }
                }
            }
        }
    }
    public void checkAccountBalance() {
        for (User user : users) {
            if (!user.isDisqualified()) {
                double accountBalance = user.getAccountBalance();
                if (accountBalance >= 0.5 * TradingApp.INITIAL_FUNDS) {
                    user.disqualify();
                }
            }
        }
}
}
