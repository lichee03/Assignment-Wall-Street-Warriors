/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dsgroup;

/**
 *
 * @author sharr
 */
import java.util.*;

public class DetectFraud {

    private static final int MAX_TRANSACTIONS_PER_DAY = 1000;
    private static final double MAX_PRICE_CHANGE_PERCENT = 10.0;
    private static final double MAX_VOLUME_CHANGE_PERCENT = 10.0;
    public static final double SUSPICIOUS_PERCENTAGE_CHANGE = 0.5;
    public static final int SUSPICIOUS_VOLUME_MULTIPLIER = 10;

    public void detectFraud(List<User> users) {
        for (User user : users) {
            List<Order> orders = user.getTransactionHistory();
            List<Transaction> transactions = createTransactions(orders, user);

            // Check for pump and dump
            Map<Stock, List<Transaction>> stockTransactionMap = groupTransactionsByStock(transactions);
            for (Map.Entry<Stock, List<Transaction>> entry : stockTransactionMap.entrySet()) {
                if (isPumpAndDump(entry.getKey(), entry.getValue())) {
                    flagPotentialFraud(entry.getValue());
                }
            }

            // Check for short selling
            for (Transaction transaction : transactions) {
                if (isShortSelling(transaction, user)) {
                    flagPotentialFraud(Collections.singletonList(transaction));
                }
            }

            // Check for high frequency trading
            if (transactions.size() > MAX_TRANSACTIONS_PER_DAY) {
                flagPotentialFraud(transactions);
            }

            // Check for suspicious price change and volume increase
            for (Transaction transaction : transactions) {
                Stock stock = transaction.getStock();
                if (isSuspiciousPriceChange(stock)) {
                    flagPotentialFraud(Collections.singletonList(transaction));
                }
                if (isSuspiciousVolume(stock)) {
                    flagPotentialFraud(Collections.singletonList(transaction));
                }
            }
        }
    }
    
    private ArrayList<Transaction> createTransactions(List<Order> orders, User user) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        for (Order order : orders) {
            Transaction transaction = new Transaction(user, order);
            transactions.add(transaction);
        }
        return transactions;
    }

    private boolean isShortSelling(Transaction transaction, User user) {
        if (transaction.getType() != Order.Type.SELL) {
            return false;
        }
        Stock stock = transaction.getStock();
        int quantity = transaction.getShares();
        return !user.getPortfolio().hasStock(stock, quantity);
    }

    private boolean isPumpAndDump(Stock stock, List<Transaction> transactions) {
        int totalVolume = 0;
        double totalValue = 0.0;
        for (Transaction transaction : transactions) {
            totalVolume += transaction.getShares();
            totalValue += transaction.getShares() * stock.getCurrentPrice();
        }
        double averagePrice = totalValue / totalVolume;
        return averagePrice > stock.getCurrentPrice() * (1 + MAX_PRICE_CHANGE_PERCENT / 100)
                || totalVolume > stock.getAverageVolume() * (1 + MAX_VOLUME_CHANGE_PERCENT / 100);
    }

    private boolean isSuspiciousPriceChange(Stock stock) {
        double oldPrice = stock.getPreviousDayClosePrice();
        double newPrice = stock.getCurrentPrice();
        double priceChange = (newPrice - oldPrice) / oldPrice;
        return Math.abs(priceChange) > SUSPICIOUS_PERCENTAGE_CHANGE;
    }

    private boolean isSuspiciousVolume(Stock stock) {
        int currentVolume = stock.getDailyVolume();
        int averageVolume = stock.getAverageVolume();
        return currentVolume > averageVolume * SUSPICIOUS_VOLUME_MULTIPLIER;
    }

    private Map<Stock, List<Transaction>> groupTransactionsByStock(List<Transaction> transactions) {
        Map<Stock, List<Transaction>> stockTransactionMap = new HashMap<>();
        for (Transaction transaction : transactions) {
            Stock stock = transaction.getStock();
            stockTransactionMap.computeIfAbsent(stock, k -> new ArrayList<>()).add(transaction);
        }
        return stockTransactionMap;
    }

    public void flagPotentialFraud(List<Transaction> transactions) {
        System.out.println("Potential fraud detected! Transaction details: " + transactions);
        // send an email or push notification to admin members
        // add the transaction to a list of flagged transactions for further review
    }
}
