/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.ArrayList;
import java.util.List;

/**
 * @author lichee
 */
public class AdminPanel { //link to User authentification
    private List<User> users;
    private TradingEngine tradingEngine;
    private boolean disqualified = false;

    public AdminPanel() {
        users = new ArrayList<>();
    }

    public boolean CheckShortSell(Stock stock, PortFolio portfolio) {
        boolean shortsell = false;
        int currentshares = portfolio.getHoldings().getOrDefault(stock, 0);


        return shortsell;
    }

    public boolean CheckMarginTrade() {
        boolean marginTrade = false;
        //method
        return marginTrade;
    }


    public void disqualifyUser(String username) {
        for (User user : users) {
            if (user.getName().equals(username)) {
                user.Disqualified();
                break;
            }
        }
    }



    public void checkAccountBalance() {
        for (User user : users) {
            if (!user.CheckDisqualified()) {
                double accountBalance = user.getPortfolio().getAccountBalance();
                if (accountBalance >= 0.5 * 500000) {
                    user.Disqualified();
                }
            }
        }
    }
}
