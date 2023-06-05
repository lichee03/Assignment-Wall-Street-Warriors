import java.util.*;

public class stockSearch {
    private PriorityQueue<Stock> stocks;
    Scanner sc = new Scanner(System.in);
    public stockSearch(PriorityQueue<Stock> stocks) {
        this.stocks = stocks;
    }
    
    public void searchStocks() {
        List<Stock> matchingStocks = new ArrayList<>();
        System.out.print("Please enter the name or symbol of the stock: ");
        String searchTerm = sc.nextLine();
        if(searchTerm.equalsIgnoreCase("all")){
            PriorityQueue<Stock> tempq = new PriorityQueue<>(stocks);
            while (!tempq.isEmpty()) {
                Stock stock = tempq.poll();
                System.out.println(stock);
            };
        }
        else{
            for (Stock stock : stocks) {
                if (kmpSearch(stock.getName(), searchTerm) || kmpSearch(stock.getSymbol(), searchTerm)) {
                    matchingStocks.add(stock);
                }
            }
            
            if (!matchingStocks.isEmpty()) {
                System.out.println("╔════════════════╦══════════════════════════════════════════════╦═════════╗");
                System.out.println("║     Symbol     ║                   Name                       ║  Price  ║");
                System.out.println("╠════════════════╬══════════════════════════════════════════════╬═════════╣");
                
                for (Stock stock : matchingStocks) {
                    System.out.printf("║ %-14s ║ %-44s ║ %7.2f ║%n", stock.getSymbol(), stock.getName(), stock.getPrice());
                }
                
                System.out.println("╚════════════════╩══════════════════════════════════════════════╩═════════╝");
            } else {
                System.out.println("Stock not found for the given search term: " + searchTerm);
            }}
        }

    private boolean kmpSearch(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        // Construct the failure table using the pattern
        int[] failure = computeFailure(pattern);

        int i = 0; // Index for text
        int j = 0; // Index for pattern

        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                if (j == m - 1) {
                    return true; // Match found
                }
                i++;
                j++;
            } else if (j > 0) {
                j = failure[j - 1];
            } else {
                i++;
            }
        }

        return false; // Match not found
    }

    private int[] computeFailure(String pattern) {
        int m = pattern.length();
        int[] failure = new int[m];

        int i = 1;
        int j = 0;

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(j)) {
                failure[i] = j + 1;
                i++;
                j++;
            } else if (j > 0) {
                j = failure[j - 1];
            } else {
                failure[i] = 0;
                i++;
            }
        }

        return failure;
    }
}
