import java.util.*;

public class dashboard {
    private List<Order> transactionHistory; 
    private User user;
    public enum displayType {
        chronological,
        price
    }
    public dashboard(User user){
        this.user = user;
    }
    private class PriceComparator implements Comparator<Order> {
        @Override
        public int compare(Order order1, Order order2) {
            return Double.compare(order1.getPrice(), order2.getPrice());
        }
    }

    public void displayAll(displayType type){
        System.out.println("Account balance: "+user.getPortfolio().getAccountBalance());
        System.out.println("Current points: "+user.points);
        System.out.println("Transaction history: ");
        if(type == displayType.price){
            Collections.sort(transactionHistory, new PriceComparator());
        }
        
        displayTransactionHistory();
        System.out.println(user.getPortfolio().getHoldings()+"     "+user.getPortfolio().getValue()); //not done yet
    }

    public void displayTransactionHistory() {


        System.out.println("----------------------");
        for (Order order : transactionHistory) {
            System.out.println("Order Type: " + order.getType());
            System.out.println("Stock: " + order.getStock().getName());
            System.out.println("Price: " + order.getPrice());
            System.out.println("Shares: " + order.getShares());
            System.out.println("----------------------");
        }
    }
}
