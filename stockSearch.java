import java.util.*;

public class stockSearch {
    private List<Stock> stocks;
    public stockSearch(List<Stock> stocks){
        this.stocks = stocks;
    }
    public void searchStocks(String searchTerm) {
        List<Stock> matchingStocks = new ArrayList<>();
    
        if (searchTerm.equalsIgnoreCase("all")==true){
            for (Stock stock : stocks) {
                System.out.printf("%-10s | %-20s | %.2f%n", stock.getSymbol(), stock.getName(), stock.getPrice());
            }
        }
        else{
        for (Stock stock : stocks) {
            if (kmpSearch(stock.getName(), searchTerm) || kmpSearch(stock.getSymbol(), searchTerm)) {
                matchingStocks.add(stock);
            }
        }
        if (!matchingStocks.isEmpty()) {
            System.out.printf("%-10s | %-20s | %.2f%n", matchingStocks.get(0).getSymbol(), matchingStocks.get(0).getName(), matchingStocks.get(0).getPrice());
        } else {
            System.out.println(("Stock not found for the given search term: " + searchTerm));
        }
    }

    }
    
    
    private boolean kmpSearch(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
    
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
