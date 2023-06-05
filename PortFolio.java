/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.HashMap;
import java.util.Map;

/**
 * @author lichee
 */
public class PortFolio {

    private final double INITIALFUND = 50000;
    public double getINITIALFUND() {
        return INITIALFUND;
    }

    private Map<Stock, Integer> holdings;
    private double accountBalance;
    private boolean disqualified;


    public PortFolio() {
        holdings = new HashMap<>();
        this.accountBalance = INITIALFUND;
        this.disqualified = false;

    }

    
    public void addStock(Stock stock, int shares,double price) {
        int currentShares = holdings.getOrDefault(stock, 0);
        holdings.put(stock, currentShares + shares);
        accountBalance-=price;

    }

    public void removeStock(Stock stock, int shares,double price) {
        int currentShares = holdings.getOrDefault(stock, 0);
        holdings.put(stock, currentShares - shares);
        accountBalance+=price;

    }

    public Map<Stock, Integer> getHoldings() {
        return holdings;
    }

    public Map<String, Integer> getStockShare() { //ignore, this is for the dashboard
        Map<String, Integer> stockHoldings = new HashMap<>();
        for (Map.Entry<Stock, Integer> entry : holdings.entrySet()) {
            Stock stock = entry.getKey();
            int shares = entry.getValue();
            stockHoldings.put(stock.getName(), shares);
        }
        return stockHoldings;
    }


    public double getValue() {
        double value = 0.0;
        for (Map.Entry<Stock, Integer> entry : holdings.entrySet()) {
            Stock stock = entry.getKey();
            int shares = entry.getValue();
            value += stock.getPrice() * shares;
        }
        return value;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public boolean hasStock(Stock stock, int quantity) {
        Integer currentQuantity = holdings.getOrDefault(stock, 0);
        return currentQuantity >= quantity;
    }


}
