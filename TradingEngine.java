import java.time.Duration;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TradingEngine {
    private List<Stock> stocks;
    private Map<Stock, Queue<UserOrder>> buyOrders;
    private Map<Stock, Queue<UserOrder>> sellOrders;
    private Map<User, Queue<UserOrder>> pendingOrders;
    private Map<Stock, Integer> lotPool; // keep track of the 500-lot pool 
    public enum Criteria {
        CRITERIA_LONGEST_TIME_LENGTH,
        CRITERIA_HIGHEST_AMOUNT_OF_MONEY
    }
    private ScheduledExecutorService executorService;
    private final int INTERVAL_HOURS = 24;
    public TradingEngine(List<Stock> stocks) {
        this.stocks = stocks;
        this.buyOrders = new HashMap<>();
        this.sellOrders = new HashMap<>();
        this.lotPool = new HashMap<>();
        this.pendingOrders = new HashMap<>();
        for (Stock stock : stocks) {
            buyOrders.put(stock, new LinkedList<>());
            sellOrders.put(stock, new LinkedList<>());
            lotPool.put(stock, 500); // Initialize the lot pool for each stock with 500 shares
        }
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::replenishLotPool, 0, INTERVAL_HOURS, TimeUnit.HOURS);;
    }
    public void executeOrder(Order order, User user) { //Need to use user instead of portfolio to access user data
        double stockPrice = order.getStock().getPrice(); //get current market stock price
        double acceptableRange = stockPrice * 0.01; // Calculate 1% of the current price
        double lowerBound = stockPrice - acceptableRange;
        double upperBound = stockPrice + acceptableRange;
        double orderPrice = order.getPrice()/order.getShares(); //Check the order price per share
        int maxShares = isInitialTradingPeriod() ? Integer.MAX_VALUE : 500;
        // Check if the stock exists in the buyOrders map
        if (!buyOrders.containsKey(order.getStock())) {
            buyOrders.put(order.getStock(), new LinkedList<>());
        }

        // Check if the stock exists in the sellOrders map
        if (!sellOrders.containsKey(order.getStock())) {
            sellOrders.put(order.getStock(), new LinkedList<>());
        }
        if(!pendingOrders.containsKey(user)){
            pendingOrders.put(user, new LinkedList<>());
        }
        if (!isTradingHours()) {
            System.out.println("Trading is currently closed. Please try again during trading hours.");
            return;
        }

        if (order.getShares() > maxShares) {
            System.out.println("Exceed order limitation");
            return;
        }
        if (orderPrice >= lowerBound && orderPrice <= upperBound) {
            UserOrder newOrder = new UserOrder(order, user);
            switch (order.getType()) {
                case BUY:
                    if (user.getPortfolio().getAccountBalance() >= order.getPrice()) {
                        buyOrders.get(order.getStock()).add(newOrder); //Adds eligible orders to buyOrders
                        executeBuyOrder(newOrder); //Executes the buy order
                    } else {
                        System.out.println("Insufficient funds, order failed");
                    }
                    break;
                case SELL:
                        if(checkSellOrderEligibilty(user.getPortfolio(), order)){
                            sellOrders.get(order.getStock()).add(newOrder); //Adds eligible orders to sellOrders
                            executeSellOrder(newOrder);//Executes the sell order
                        }
                    break;
            }  
        } else {
            System.out.println("Price is outside the acceptable range, order failed");
        }
    }

    public void executeBuyOrder(UserOrder userOrder){ //Accepts a buy order, checks if there is a matching sell order, and executes the order. If none, allocate shares from market. If none, order remains in buyOrders.
        Queue<UserOrder> orderQueue  = new LinkedList<>();

        Order currentOrder = userOrder.getOrder(); //ease of access
        Stock stock = currentOrder.getStock(); //ease of access
        
        boolean orderExecuted = false;
        orderQueue = sellOrders.get(stock); //gets the sellOrders for the stock

        while (!orderQueue.isEmpty()) {
            UserOrder orderFromSellOrder = orderQueue.poll(); //gets the sellOrder from the list
            if (orderFromSellOrder.getOrder().getPrice() == currentOrder.getPrice() && orderFromSellOrder.getOrder().getShares() == currentOrder.getShares()) { //match found
                userOrder.getUser().getPortfolio().addStock(stock, currentOrder.getShares(), currentOrder.getPrice()); //add stock to buyer's portfolio
                orderFromSellOrder.getUser().getPortfolio().removeStock(stock, currentOrder.getShares(), currentOrder.getPrice()); //remove stock from seller's portfolio
                sellOrders.get(stock).remove(orderFromSellOrder); //remove the  order from sellOrders
                buyOrders.get(stock).remove(orderFromSellOrder); //remove the  order from buyOrders
                pendingOrders.get(orderFromSellOrder.getUser()).remove(orderFromSellOrder); //remove the pending sell order from the seller's pendingOrders
                if(pendingOrders.get(userOrder.getUser()).contains(userOrder)){ //check if the order is in pendingOrders
                    pendingOrders.get(userOrder.getUser()).remove(userOrder); //remove the pending buy order from the buyer's pendingOrders
                }
                userOrder.getUser().getTransactionHistory().add(currentOrder); //add the order to the buyer's transaction history
                orderFromSellOrder.getUser().getTransactionHistory().add(orderFromSellOrder.getOrder()); //add the order to the seller's transaction history
                System.out.println("Buy Order (user) completed");
                orderExecuted = true;
                break;
            } 
            }
        if(!orderExecuted){
            int remainingShares = lotPool.get(stock); 
                if (remainingShares >= currentOrder.getShares()) { // Checks for shares available in the lot-pool
                    userOrder.getUser().getPortfolio().addStock(stock, currentOrder.getShares(), currentOrder.getPrice()); // Adds the stock to the buyer's portfolio
                    if(isInitialTradingPeriod())
                    lotPool.put(stock, remainingShares - currentOrder.getShares()); // Updates the lot-pool
                    buyOrders.get(stock).remove(userOrder); //Removes the buy order from the buyOrders list
                    if(pendingOrders.get(userOrder.getUser()).contains(userOrder)){ //check if the order is in pendingOrders
                        pendingOrders.get(userOrder.getUser()).remove(userOrder); //remove the pending buy order from the buyer's pendingOrders
                    }
                    userOrder.getUser().getTransactionHistory().add(currentOrder); //add the order to the buyer's transaction history
                    System.out.println("Buy Order (market) completed");

                } else {
                    pendingOrders.get(userOrder.getUser()).add(userOrder); //Adds the order to the pendingOrders list
                    System.out.println("No sell orders or market share available. Order is pending.");
                }
        }
    }

    public void executeSellOrder(UserOrder userOrder){ //Accepts a sell order, checks if there is a matching buy order, and executes the order. If none, order remains in sellOrders.
            Queue<UserOrder> orderQueue = new LinkedList<>();

            Order currentOrder = userOrder.getOrder();
            Stock stock = currentOrder.getStock(); //ease of access
            
            orderQueue = buyOrders.get(stock); //gets the buyOrders for the stock of the order

            while(!orderQueue.isEmpty()){
                UserOrder orderFromBuyOrder = orderQueue.poll(); //gets the buyOrder from the list
                if (orderFromBuyOrder.getOrder().getPrice() == currentOrder.getPrice() && orderFromBuyOrder.getOrder().getShares() == currentOrder.getShares()) { //match found
                    userOrder.getUser().getPortfolio().removeStock(stock, currentOrder.getShares(), currentOrder.getPrice()); //remove stock from seller
                    orderFromBuyOrder.getUser().getPortfolio().addStock(stock, currentOrder.getShares(), currentOrder.getPrice()); //add stock to buyer
                    sellOrders.get(stock).remove(orderFromBuyOrder); //remove the pending order from sellOrders
                    buyOrders.get(stock).remove(userOrder); //remove the pending order from buyOrders
                    pendingOrders.get(orderFromBuyOrder.getUser()).remove(orderFromBuyOrder); //remove the pending buy order from the buyer's pendingOrders
                    if(pendingOrders.get(userOrder.getUser()).contains(userOrder)){ //check if the order is in pendingOrders
                        pendingOrders.get(userOrder.getUser()).remove(userOrder); //remove the pending buy order from the seller's pendingOrders
                    }
                    userOrder.getUser().getTransactionHistory().add(currentOrder); //add the order to the seller's transaction history
                    break;
                } 
                else{
                    pendingOrders.get(userOrder.getUser()).add(userOrder); //Adds the order to the pendingOrders list
                    System.out.println("No buy orders available. Order is pending.");
                }
            }
        }
    
    public boolean checkSellOrderEligibilty(PortFolio portfolio, Order order) {
        
            double totalPrice = order.getStock().getPrice() * order.getShares();
    
            if (order.getPrice() <= totalPrice) {
                int currentShares = portfolio.getHoldings().getOrDefault(order.getStock(), 0);
                if (currentShares >= order.getShares()) {
                    return true;
                } else {
                    System.out.println("Not enough shares to sell. Order failed.");
                    return false;
                }
            } else {
                System.out.println("Sell Order price must be equal to or less than the current stock price. Sell Order failed.");
                return false;
            }
        }

        public void removingPendingOrder(User user, Criteria criteria){
            if (criteria == Criteria.CRITERIA_LONGEST_TIME_LENGTH){
                removeLongestPendingOrder(user);
            }
            else if (criteria == Criteria.CRITERIA_HIGHEST_AMOUNT_OF_MONEY){
                removePriciestOrder(user);
            }
        }

        public void removeLongestPendingOrder(User user) {
            if (user!=null && pendingOrders.get(user)!=null && !pendingOrders.get(user).isEmpty()){
                if (pendingOrders.get(user).peek().getOrder().getType() == Order.Type.BUY)
                buyOrders.get(pendingOrders.get(user).peek().getOrder().getStock()).remove(pendingOrders.get(user).peek()); //remove the longest pending buy order
                else
                sellOrders.get(pendingOrders.get(user).peek().getOrder().getStock()).remove(pendingOrders.get(user).peek()); //remove the longest pending sell order
                pendingOrders.get(user).poll(); //remove the longest pending order
            }
        }

        public void removePriciestOrder(User user){
            double highestPrice = 0;
            UserOrder highestOrder = null;
            if (user!=null && pendingOrders.get(user)!=null && !pendingOrders.get(user).isEmpty()){
                for (UserOrder order : pendingOrders.get(user)) {
                    if (order.getOrder().getPrice() > highestPrice) {
                        highestPrice = order.getOrder().getPrice();
                        highestOrder = order;
                    }
                }
                if (highestOrder.getOrder().getType() == Order.Type.BUY)
                buyOrders.get(highestOrder.getOrder().getStock()).remove(highestOrder); //remove the highest priced buy order
                else
                sellOrders.get(highestOrder.getOrder().getStock()).remove(highestOrder); //remove the highest priced sell order
                pendingOrders.get(user).remove(highestOrder); //remove the highest priced order
            }
        }

    public boolean isTradingHours() {
        // Get the current time
        LocalTime currentTime = LocalTime.now();
        LocalTime startMorning = LocalTime.of(9, 0);
        LocalTime endMorning = LocalTime.of(12, 30);
        LocalTime startAfternoon = LocalTime.of(14, 30);
        LocalTime endAfternoon = LocalTime.of(19, 30);
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
            return false;
        } else
            return true;
    }
  
    public Map<User, Queue<UserOrder>> getPendingOrders() {
        return pendingOrders;
    }
    public Map<Stock, Queue<UserOrder>> getSellOrders() {
        return sellOrders;
    }

    public void getStocklist(){
        System.out.println(stocks);
    }

    public void replenishLotPool() { //this is just the basic method, havent implemented a timer since idk how its gonna be used :p
        for (Stock stock : stocks) {
            lotPool.put(stock, 500); // Replenish the lot pool for each stock with 500 shares
        }
    }

    public void displaySuggestedPrice(Stock stock){
        double stockPrice = stock.getPrice(); //get current market stock price
        double acceptableRange = stockPrice * 0.01; // Calculate 1% of the current price
        double lowerBound = stockPrice - acceptableRange;
        double upperBound = stockPrice + acceptableRange;
        System.out.println("Suggested price range for " + stock.getName() + ": " + lowerBound + " - " + upperBound);
    }
}

class UserOrder{
    private Order order;
    private User user;


    public UserOrder(Order order, User user){
        this.order = order;
        this.user = user;
    }
    public Order getOrder() {
        return order;
    }
    public User getUser() {
        return user;
    }
}
