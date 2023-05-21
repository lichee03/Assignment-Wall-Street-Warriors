/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lichee
 */
public class PortFolio {

    private Map<Stock, Integer> holdings;
    private double accountBalance;
    private boolean disqualified;
    private final int INITIALFUND=500000;

    public PortFolio() {
        holdings = new HashMap<>();
        this.accountBalance = INITIALFUND;
        this.disqualified = false;
    }

    public void addStock(Stock stock, int shares) {
        int currentShares = holdings.getOrDefault(stock, 0);
        holdings.put(stock, currentShares + shares);
    }

    public void removeStock(Stock stock, int shares) {
        int currentShares = holdings.getOrDefault(stock, 0);
        if (currentShares >= shares) {
            holdings.put(stock, currentShares - shares);
        }
    }

    public Map<Stock, Integer> getHoldings() {
        return holdings;
    }

    public double getValue() {  // profit
        double value = 0.0;
        for (Map.Entry<Stock, Integer> entry : holdings.entrySet()) {
            Stock stock = entry.getKey();
            int shares = entry.getValue();
            value += stock.getPrice() * shares;
            accountBalance+=value;
        }
        return value;
    }
    public double getAccountBalance() {
        return accountBalance;
    }

}
