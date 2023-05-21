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
    private List<Order> pendingOrders;
    private LocalDateTime timestamp;

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
        while(pendingOrders.size()!=0) {

            for (int i = 0; i < pendingOrders.size(); i++) {
                Order pendingorder = pendingOrders.get(i);
                if (tradingEngine.executePendingOrder(pendingorder, user.getPortfolio())) {
                    pendingOrders.remove(i);
                }
            }
        }

    }


}
