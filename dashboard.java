import java.util.*;
import java.util.Map.Entry;

public class Dashboard {
    private PortFolio pf;

    public enum displayType {
        CHRONOLOGICAL,
        PRICE
    }

    public Dashboard(PortFolio pf) {
        this.pf = pf;
    }

    private class PriceComparator implements Comparator<Order> {
        @Override
        public int compare(Order order1, Order order2) {
            return Double.compare(order1.getPrice(), order2.getPrice());
        }
    }

    public void displayAll(displayType type) {
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                             DASHBOARD                            ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Account balance: %-47.2f ║%n", pf.getAccountBalance());
        System.out.printf("║ Current points: %-48s ║%n", String.format("%-9s", pf.getUser().points));
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Holdings:                                                        ║");
        Map<String, Integer> holdings = pf.getStockShare();
        System.out.println("║ Stock name                   ║ Shares                            ║");
        for (Entry<String, Integer> entry : holdings.entrySet()) {
            String stock = entry.getKey();
            int shares = entry.getValue();
            System.out.printf("║ %-28s ║ %-10d                        ║%n", stock, shares);
        }
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");

        System.out.println("║ Transaction history:                                             ║");
        Database db = new Database();
        List<Order> orderHistory = db.retriveOrderHistory(pf.getUser());
        if(orderHistory.isEmpty()){
            System.out.println("║ No past transactions.                                            ║");
        }
        else{
            for (Order order : orderHistory){
                if(order.getStatus().equalsIgnoreCase("Completed")){
                    if (type == displayType.PRICE) {
                        Collections.sort(orderHistory, new PriceComparator());
                        String line = "══════════════════════════════════════════════════════════════════";
                        System.out.println(centerAlignWithBorders("Order Type: " + order.getType()));
                        System.out.println(centerAlignWithBorders("Stock: " + order.getStock().getName()));
                        System.out.println(centerAlignWithBorders("Price: " + order.getPrice()));
                        System.out.println(centerAlignWithBorders("Shares: " + order.getShares()));
                        System.out.println(centerAlignWithBorders(line));
                    }
                    else{
                        String line = "══════════════════════════════════════════════════════════════════";
                        System.out.println(centerAlignWithBorders("Order Type: " + order.getType()));
                        System.out.println(centerAlignWithBorders("Stock: " + order.getStock().getName()));
                        System.out.println(centerAlignWithBorders("Price: " + order.getPrice()));
                        System.out.println(centerAlignWithBorders("Shares: " + order.getShares()));
                        System.out.println(centerAlignWithBorders(line));
                    }
                }
            }
        }

        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
    }

    public static String centerAlignWithBorders(String text) {
        int totalSpaces = 66 - text.length();
        int leftSpaces = totalSpaces / 2;
        int rightSpaces = totalSpaces - leftSpaces;
        StringBuilder sb = new StringBuilder();
        sb.append("║");
        for (int i = 0; i < leftSpaces; i++) {
            sb.append(" ");
        }
        sb.append(text);
        for (int i = 0; i < rightSpaces; i++) {
            sb.append(" ");
        }
        sb.append("║");
        return sb.toString();
    }
}