/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.time.Duration;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author lichee
 */
public class TradingEngine {

    private List<Stock> stocks;
    private Map<Stock, List<Order>> buyOrders;
    private Map<Stock, List<Order>> sellOrders;
    private TradingApp tradingApp;
    private Map<Stock, Integer> lotPool; // keep track of the 500-lot pool 
    private static List<Order> pendingOrders;


    public enum Criteria {
        CRITERIA_LONGEST_TIME_LENGTH,
        CRITERIA_HIGHEST_AMOUNT_OF_MONEY
    }

    public TradingEngine(List<Stock> stocks) {
        this.stocks = stocks;     //holding currrent available stock
        this.buyOrders = new HashMap<>();
        this.sellOrders = new HashMap<>();
        this.lotPool = new HashMap<>();
        for (Stock stock : stocks) {
            buyOrders.put(stock, new ArrayList<>()); //each stock will be assign a list
            sellOrders.put(stock, new ArrayList<>());
            lotPool.put(stock, 500); // Initialize the lot pool for each stock with 500 shares

        }
    }

    public void executeOrder(Order order, User user) {
        double price = order.getStock().getPrice(); //get current market stock price
        double acceptableRange = price * 0.01; // Calculate 1% of the current price
        double lowerBound = price - acceptableRange;
        double upperBound = price + acceptableRange;
        double orderPrice = order.getPrice();
        Order.Position position = order.getPosition(); //get position (market/pending)
        int maxShares = isInitialTradingPeriod() ? Integer.MAX_VALUE : 500;
        if (!isTradingHours()) {
            System.out.println("Trading is currently closed. Please try again during trading hours.");
            return;
        }
        if (order.getShares() > maxShares) {
            System.out.println("Exceed order limitation");
            return;
        }
        switch (position) {
            case MARKET:
                if (orderPrice >= lowerBound && orderPrice <= upperBound) {
                    if (order.getType() == Order.Type.BUY) {
                        buyOrders.get(order.getStock()).add(order); //find order whether its available or not , if available, add order
                        tryExecuteBuyOrders(order.getStock(), user.getPortfolio());
                    } else {
                        sellOrders.get(order.getStock()).add(order);
                        tryExecuteSellOrders(order.getStock(), user.getPortfolio());
                    }
                user.getTransactionHistory().add(order); // Adds the transaction to the user's transaction history
                } else {
                    System.out.println("Price is outside the acceptable range, order failed");
                }
            case PENDING:
                pendingOrders.add(order);
                CheckPendingOrder(pendingOrders, user.getPortfolio());
        }
    }


    public void CheckPendingOrder(List<Order> pendingOrders, PortFolio portfolio) {  //execute if meet requirement, else store in list
        for (int i = 0; i < pendingOrders.size(); i++) {
            Order pendingorder = pendingOrders.get(i);
            if (pendingorder.getStock().getPrice() == pendingorder.getPrice()) {

                if (pendingorder.getType() == Order.Type.BUY) {
                    buyOrders.get(pendingorder.getStock()).add(pendingorder); //find order whether its available or not , if available, add order
                    tryExecuteBuyOrders(pendingorder.getStock(), portfolio);

                } else {
                    sellOrders.get(pendingorder.getStock()).add(pendingorder);
                    //if() if the sell order being bought by other user, baru execute
                    tryExecuteSellOrders(pendingorder.getStock(), portfolio);
                }
            }
        }
    }

    public List<Order> getPendingOrders() {
        return pendingOrders;
    }

    public Map<Stock, List<Order>> getBuyOrders() {
        return buyOrders;
    }

    public Map<Stock, List<Order>> getSellOrders() {
        return sellOrders;
    }


    public void cancelPendingOrder(Criteria criteria) {
        Criteria criterias = criteria;

        if (!pendingOrders.isEmpty()) {
            switch (criterias) {
                case CRITERIA_LONGEST_TIME_LENGTH:
                    // Sort the pending orders based on time length in descending order
                    pendingOrders.sort(Comparator.comparing(Order::getTime).reversed());
                    Order canceledOrder = pendingOrders.remove(0);
                    System.out.println("Canceled order: " + canceledOrder.getStock().getSymbol() + " - " + canceledOrder.getShares() + " shares at " + canceledOrder.getPrice());
                    break;
                case CRITERIA_HIGHEST_AMOUNT_OF_MONEY:
                    pendingOrders.sort(Comparator.comparing(Order::getValue).reversed());
                    canceledOrder = pendingOrders.remove(0);
                    System.out.println("Canceled order: " + canceledOrder.getStock().getSymbol() + " - " + canceledOrder.getShares() + " shares at " + canceledOrder.getPrice());
                    break;
            }

        } else {
            System.out.println("No pending order available.");
        }
    }

    public void tryExecuteBuyOrders(Stock stock, PortFolio portfolio) {
        List<Order> orders = buyOrders.get(stock);
        double marketPrice = stock.getPrice();
        boolean initialTradingPeriodOver;
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if (order.getPrice() >= marketPrice) {
                int currentShares = portfolio.getHoldings().getOrDefault(stock, 0); //called on the holdings map to retrieve the value associated with the stock key. If the stock key is present in the map, it returns the corresponding value
                double totalPrice = order.getPrice() * order.getShares();
                if (portfolio.getAccountBalance() >= totalPrice) {
                    if (sellOrders.get(stock).isEmpty()) { // if there are no sell orders for that stock
                        int remainingShares = lotPool.get(stock);
                        if (remainingShares >= order.getShares() && marketPrice <= order.getPrice()) { //if shares from 500-lot is enough for the order and the price is ok
                            portfolio.addStock(stock, order.getShares(), order.getPrice());
                            lotPool.put(stock, remainingShares - order.getShares());
                            orders.remove(i);
                            this.buyOrders.get(order.getStock()).remove(order);
                            i--;
                    } else {
                        System.out.println("Insufficient shares available in the lot pool or market price exceeds the order price. Order failed.");
                        orders.remove(i);
                        this.buyOrders.get(order.getStock()).remove(order);
                        i--;
                    }
                    portfolio.addStock(stock, order.getShares(), order.getPrice());
                    orders.remove(i);
                    this.buyOrders.get(order.getStock()).remove(order);
                    i--;
                } else {
                    System.out.println("Current Account Balance not enough. Order Failed.");
                    orders.remove(i);
                    this.buyOrders.get(order.getStock()).remove(order);
                    i--;
                }
            } else {
                System.out.println("Buy Order marketPrice must be more than or equal to the current stock marketPrice. Order Failed");
                orders.remove(i);
                this.buyOrders.get(order.getStock()).remove(order);
                i--;
            }
        }
    }

    public void tryExecuteSellOrders(Stock stock, PortFolio portfolio) {//update order book
        List<Order> orders = sellOrders.get(stock);
        double price = stock.getPrice();
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if (order.getPrice() <= price) { // is this line needed?
                int currentShares = portfolio.getHoldings().getOrDefault(stock, 0);
                if (currentShares >= order.getShares()) {
                    portfolio.removeStock(stock, order.getShares(), order.getPrice());
                    orders.remove(i);
                    this.sellOrders.get(order.getStock()).remove(order);
                    i--;
                } else {
                    System.out.println("Not enough share to sell. Order failed");
                    orders.remove(i);
                    this.sellOrders.get(order.getStock()).remove(order);
                    i--;
                }
            } else {
                System.out.println("Sell Order price must be equal or less than the current stock price. Order failed.");
                orders.remove(i);
                this.sellOrders.get(order.getStock()).remove(order);
                i--;
            }
        }
    }

    public boolean isTradingHours() {
        // Get the current time
        LocalTime currentTime = LocalTime.now();

        LocalTime startMorning = LocalTime.of(9, 0);
        LocalTime endMorning = LocalTime.of(12, 30);
        LocalTime startAfternoon = LocalTime.of(14, 30);
        LocalTime endAfternoon = LocalTime.of(17, 0);

        // Check if the current time is within the trading hours
        boolean isMorningSession = currentTime.isAfter(startMorning) && currentTime.isBefore(endMorning);
        boolean isAfternoonSession = currentTime.isAfter(startAfternoon) && currentTime.isBefore(endAfternoon);

        return isMorningSession || isAfternoonSession;
    }

    public boolean isInitialTradingPeriod() {
        LocalDateTime sessionStart = LocalDateTime.of(2023, 5, 22, 9, 00);
        LocalDateTime now = LocalDateTime.now();
        long noOfDays = Duration.between(sessionStart, now).toDays();
        if (noOfDays > 3) {
            return true;
        } else
            return false;
    }

    public double DisplaySuggestedPrice(String symbol) {
        double price;
        for (int i = 0; i < stocks.size(); i++) {
            Stock stock = stocks.get(i);
            if (stock.getSymbol() == symbol) {
               return price= stock.getPrice();
            }

        }
    return 0;
    }



    public void updatePrices() {
        for (Stock stock : stocks) {
            // Update the stock price based on some market data source
            double newPrice = 1; // Get the new price from some market data source
            stock.setPrice(newPrice);
            tryExecuteBuyOrders(stock, new PortFolio()); // clear the previous user
            tryExecuteSellOrders(stock, new PortFolio());
        }
    }

    public void replenishLotPool() { //this is just the basic method, havent implemented a timer since idk how its gonna be used :p
        for (Stock stock : stocks) {
            lotPool.put(stock, 500); // Replenish the lot pool for each stock with 500 shares
        }
    }
    
}
