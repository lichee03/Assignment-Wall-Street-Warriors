/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author lichee
 */
public class TradingEngine {

    private List<Stock> stocks;
    private Map<Stock, List<Order>> buyOrders;
    private Map<Stock, List<Order>> sellOrders;
    private TradingApp tradingApp;
    private Map<Stock, Integer> lotPool; // keep track of the 500-lot pool 
    private static List<Order> pendingOrders;
    private final int MAX_SHARES_PER_ORDER=500;
    private boolean isInitialTradingPeriod = true;
    

    public TradingEngine(List<Stock> stocks) {
        this.stocks = stocks;     //holding currrent available stock
        this.buyOrders = new HashMap<>();
        this.sellOrders = new HashMap<>();
        for (Stock stock : stocks) {
            buyOrders.put(stock, new ArrayList<>()); //each stock will be assign a list
            sellOrders.put(stock, new ArrayList<>());
        }
    }

    public void executeOrder(Order order, PortFolio portfolio) {
        double price = order.getStock().getPrice(); //get current market stock price
        double acceptableRange = price * 0.01; // Calculate 1% of the current price
        double lowerBound = price - acceptableRange;
        double upperBound = price + acceptableRange;
        double orderPrice = order.getPrice();
        pendingOrders= tradingApp.getPendingOrders();
        if(time pass three days )
            isInitialTradingPeriod=false;
            
        if (!isTradingHours()) {
            System.out.println("Trading is currently closed. Please try again during trading hours.");
            return;
        }
        if (orderPrice >= lowerBound && orderPrice <= upperBound) {
            if (order.getType() == Order.Type.BUY) {
                buyOrders.get(order.getStock()).add(order); //find order whether its available or not , if available, add order
                tryExecuteBuyOrders(order.getStock(), portfolio);

            } else {
                sellOrders.get(order.getStock()).add(order);
                tryExecuteSellOrders(order.getStock(), portfolio);
            }
        } else {
            // Price is outside the acceptable range, handle accordingly (e.g., display an error message)
        }
    }
    public void executePendingOrder(Order order, PortFolio portfolio) {
        double price = order.getStock().getPrice(); //get current market stock price
        double acceptableRange = price * 0.01; // Calculate 1% of the current price
        double lowerBound = price - acceptableRange;
        double upperBound = price + acceptableRange;
        double orderPrice = order.getPrice();
        pendingOrders= tradingApp.getPendingOrders();
        if(time pass three days )
            isInitialTradingPeriod=false;
            
        if (!isTradingHours()) {
            System.out.println("Trading is currently closed. Please try again during trading hours.");
            return;
        }
        if (orderPrice >= lowerBound && orderPrice <= upperBound) {
            if (order.getType() == Order.Type.BUY) {
                buyOrders.get(order.getStock()).add(order); //find order whether its available or not , if available, add order
                tryExecuteBuyOrders(order.getStock(), portfolio);

            } else {
                sellOrders.get(order.getStock()).add(order);
                tryExecuteSellOrders(order.getStock(), portfolio);
            }
        } else {
            // Price is outside the acceptable range, handle accordingly (e.g., display an error message)
        }
    }
    public boolean tryExecuteBuyOrders(Stock stock, PortFolio portfolio) {
        List<Order> orders = buyOrders.get(stock);
        double price = stock.getPrice();
        boolean initialTradingPeriodOver;
        int maxShares = (initialTradingPeriod) ? 500 : Integer.MAX_VALUE; // Check if the initial trading period is over
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if (order.getPrice() >= price) {
                int currentShares = portfolio.getHoldings().getOrDefault(stock, 0); //called on the holdings map to retrieve the value associated with the stock key. If the stock key is present in the map, it returns the corresponding value 
                double totalPrice = order.getPrice() * order.getShares();
                if (portfolio.getValue() >= totalPrice) {
                    portfolio.addStock(stock, order.getShares());
                    orders.remove(i);
                    i--;
                }
            }
        }return true;
    }

    public void tryExecuteSellOrders(Stock stock, PortFolio portfolio) {
        List<Order> orders = sellOrders.get(stock);
        double price = stock.getPrice();
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if (order.getPrice() <= price) {
                int currentShares = portfolio.getHoldings().getOrDefault(stock, 0);
                if (currentShares >= order.getShares()) {
                    portfolio.removeStock(stock, order.getShares());
                    orders.remove(i);
                    i--;
                }
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

    public void AutoMatching(Stock stock, Order order) {
//        List<Order> sellOrders = this.sellOrders.get(stock);
        List<Order> buyOrders = this.buyOrders.get(stock);

//        double price = stock.getPrice();
//        for (int i = 0; i < sellOrders.size(); i++) {
//            Order sellOrder = sellOrders.get(i);
//            if (sellOrder.getPrice() <= price) {
//                int sellShares = sellOrder.getShares();
        for (int j = 0; j < buyOrders.size(); j++) {
            Order buyOrder = buyOrders.get(j);

            if (buyOrder.getPrice() >= sellOrder.getPrice()) {
                int buyShares = buyOrder.getShares();

                int matchedShares = Math.min(sellShares, buyShares);
                double tradeValue = matchedShares * sellOrder.getPrice();

                portfolio.addStock(stock, matchedShares);
                portfolio.subtractValue(tradeValue);

                sellShares -= matchedShares;
                buyShares -= matchedShares;

//                        if (sellShares == 0) {
//                            sellOrders.remove(i);
//                            i--;
//                            break;
//                        }
                if (buyShares == 0) {
                    buyOrders.remove(j);
                    j--;
                }
            }
        }
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
}
