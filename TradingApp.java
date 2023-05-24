/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.List;
import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * @author lichee
 */
public class TradingApp {

    private static List<User> users;
    private TradingEngine tradingEngine;
    private List<Order> pendingOrders;


    public TradingApp(List<User> users, TradingEngine tradingEngine) {
        this.users = users;
       this.tradingEngine = tradingEngine;
    }

    public User login(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public void placeOrder(User user, Order order) {
        tradingEngine.executeOrder(order, user.getPortfolio());

    }
    public void UpdateOrderBook(){
        System.out.println("Current Order Book: ");
        tradingEngine.getSellOrders();
    }
    public void updatePendingOrder(){
        System.out.println("Current Order Boook: ");
        tradingEngine.getPendingOrders();
    }

    public void updateAllUserPoints(){
        for (User user : users){
            updatePoints(user);
        }
    }
    private void updatePoints(User user) { //to be used with the API, updates the points for ***a user***
        double pnl = 0.0;
        for (Order order : user.getTransactionHistory()) { //Iterates thru all the user's buy/sell orders
            Stock stock = order.getStock();
            int shares = order.getShares();
            double purchasePrice = order.getPrice(); //price of stock during the order
            double currentPrice = stock.getPrice(); //current price of the stock
            if (order.getType() == Order.Type.BUY) {
                pnl += (currentPrice - purchasePrice) * shares;
            } else if (order.getType() == Order.Type.SELL) {
                pnl -= (currentPrice - purchasePrice) * shares;
            }
        }
        if (pnl > 0) {
            user.points += (int) Math.round(pnl);
        } else {
            user.points -= (int) Math.round(Math.abs(pnl));
        }
    }
    



}
