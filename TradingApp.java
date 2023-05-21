/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import Admin_Trading.Order.Type;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Comparator;

/**
 *
 * @author lichee
 */
public class TradingApp {

    private static List<User> users;
    private TradingEngine tradingEngine;
    private static List<Order> pendingOrders;
    private LocalDateTime timestamp;

    public enum Criteria {
        CRITERIA_LONGEST_TIME_LENGTH,
        CRITERIA_HIGHEST_AMOUNT_OF_MONEY
    }
    public TradingApp(){
        
    }
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
    public void placePendingOrder(User user, Order order) {
        pendingOrders.add(order);
          //if meet requirement, exceute the order;

    }
    

    public static List<Order> getPendingOrders() {
        return pendingOrders;
    }

    public void cancelPenidngOrder(Criteria criteria) {
        if (criteria.compareTo(Criteria.CRITERIA_LONGEST_TIME_LENGTH) == 0) {
            // Sort the pending orders based on time length in descending order
            pendingOrders.sort(Comparator.comparing(Order::getTime).reversed());
            Order canceledOrder = pendingOrders.remove(0);
            System.out.println("Canceled order: " + canceledOrder.getStock().getSymbol() + " - " + canceledOrder.getShares() + " shares at " + canceledOrder.getPrice());
        } else if (criteria.compareTo(Criteria.CRITERIA_HIGHEST_AMOUNT_OF_MONEY) == 0) {
            pendingOrders.sort(Comparator.comparing(Order::getValue).reversed());
            Order canceledOrder = pendingOrders.remove(0);
            System.out.println("Canceled order: " + canceledOrder.getStock().getSymbol() + " - " + canceledOrder.getShares() + " shares at " + canceledOrder.getPrice());
        }

    }
//    public static void main(String[] args) {
//        TradingApp ta= new TradingApp();
//        Type type= Type.BUY;
//        Stock stock = new Stock("Google","Apple",5.12);
//        Order order = new Order(stock,type,500,5.14);
//        User user= new User("LiChee","lichee03@gmail.com","12345");
//        ta.placeOrder(user, order);
//}
}
