/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/**
 * @author lichee
 */
public class TradingApp {

    private List<User> users;
    private TradingEngine tradingEngine;
    private Set<Stock> stocksToUpdate;
    private ScheduledExecutorService executorService;
    private final int INTERVAL_MINUTES = 5;


    public TradingApp(TradingEngine tradingEngine, List<User> users) {
       this.users = users;
       this.tradingEngine = tradingEngine;
       stocksToUpdate = new HashSet<>();
       executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::updatePricesAndPoints, 0, INTERVAL_MINUTES, TimeUnit.MINUTES); //runs updatePricesAndPoints method every 5 minutes
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
        tradingEngine.executeOrder(order, user);
    }
    public void UpdateOrderBook(){
        System.out.println("Current Order Book: ");
        tradingEngine.getSellOrders();
    }

    public void updatePricesAndPoints(){
        updatePrices();
        for (User user : users){
            updatePoints(user);
        }
    }
    public void updatePoints(User user) { //ipdates points for a user
        user.points = (((user.getPortfolio().getValue()+user.getPortfolio().getAccountBalance())-50000)/50000)*100;
    }

    public void updatePrices() {
        // Collect all the stocks owned by users (so we dont need to update EVERY stock, just the ones that are owned)
        for (User user : users) {
            Map<Stock, Integer> holdings = user.getPortfolio().getHoldings();
            stocksToUpdate.addAll(holdings.keySet());
        }
        // Update prices for the selected stocks
        for (Stock stock : stocksToUpdate) {
            stock.getPrice();
        }
    }
    
}
