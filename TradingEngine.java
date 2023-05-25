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
    private List<Order> pendingOrders;

    private Stocklist2 stocklist;

    public enum Criteria {
        CRITERIA_LONGEST_TIME_LENGTH,
        CRITERIA_HIGHEST_AMOUNT_OF_MONEY
    }

    public TradingEngine(List<Stock> stocks) {
        this.stocks = stocks;
        this.buyOrders = new HashMap<>();
        this.sellOrders = new HashMap<>();
        this.pendingOrders = new ArrayList<>();

        for (Stock stock : stocks) {
            buyOrders.put(stock, new ArrayList<>());
            sellOrders.put(stock, new ArrayList<>());
        }
    }

    public void executeOrder(Order order, PortFolio portfolio) {
        String symbol = order.getStock().getSymbol();
        double price = order.getStock().getPrice(); //get current market stock price
        double acceptableRange = price * 0.01; // Calculate 1% of the current price
        double lowerBound = price - acceptableRange;
        double upperBound = price + acceptableRange;
        double orderPrice = order.getPrice();
        Order.Position position = order.getPosition();
        int maxShares = isInitialTradingPeriod() ? Integer.MAX_VALUE : 500;
        // Check if the stock exists in the buyOrders map
        if (!buyOrders.containsKey(order.getStock())) {
            buyOrders.put(order.getStock(), new ArrayList<>());
        }

        // Check if the stock exists in the sellOrders map
        if (!sellOrders.containsKey(order.getStock())) {
            sellOrders.put(order.getStock(), new ArrayList<>());
        }
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
                if (order.getType() == Order.Type.BUY) {
                    buyOrders.get(order.getStock()).add(order); //find order whether its available or not , if available, add order
                    tryExecuteBuyOrders(order.getStock(), portfolio);

                } else {
                    sellOrders.get(order.getStock()).add(order);
                    tryExecuteSellOrders(order.getStock(), portfolio);
                }
                break;
            case LIMIT:
                if (orderPrice >= lowerBound && orderPrice <= upperBound) {
                    pendingOrders.add(order);
                    CheckPendingOrder(pendingOrders, portfolio);
                } else {
                    System.out.println("Price is outside the acceptable range, order failed");
                }
                break;
        }
    }

    public void tryExecuteBuyOrders(Stock stock, PortFolio portfolio) {
        List<Order> orders = buyOrders.get(stock);
        double price = stock.getPrice();
        for (int i = 0; i < orders.size(); i++) {
            System.out.println("Size"+orders.size());
            Order order = orders.get(i);
            if (order.getPrice() >= price) {
                double totalPrice = order.getPrice() * order.getShares();
                if (portfolio.getAccountBalance() >= totalPrice) {
                    portfolio.addStock(stock, order.getShares(), order.getPrice());
                    orders.remove(i);
                    this.buyOrders.get(order.getStock()).remove(order);
                    i--;
                    System.out.println("Buy Order completed");
                } else {
                    System.out.println("Current Account Balance not enough.Order Failed.");
                    orders.remove(i);
                    this.buyOrders.get(order.getStock()).remove(order);
                    i--;
                }
            } else {
                System.out.println("Buy Order price must be more than or equal to the current stock price. com.example.testing1.WallStreet.Stock.Order Failed");
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
                    System.out.println("Sell Order Completed.");
                } else {
                    System.out.println("Not enough share to sell.Order failed");
                    orders.remove(i);
                    this.sellOrders.get(order.getStock()).remove(order);
                    i--;
                }
            } else {
                System.out.println("Sell Order price must be equal or less than the current stock price. com.example.testing1.WallStreet.Stock.Order failed.");
                orders.remove(i);
                this.sellOrders.get(order.getStock()).remove(order);
                i--;
            }
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
            return true;
        } else
            return false;
    }

    public double DisplaySuggestedPrice(String symbol) {
        double price;
        for (int i = 0; i < stocks.size(); i++) {
            Stock stock = stocks.get(i);
            if (stock.getSymbol() == symbol) {
                return price = stock.getPrice();
            }

        }
        return 0;
    }


    public void updatePrices() {
        for (Stock stock : stocks) {
            // Update the stock price based on some market data source
            stock.getPrice();
            tryExecuteBuyOrders(stock, new PortFolio()); // clear the previous user
            tryExecuteSellOrders(stock, new PortFolio());
        }
    }
    public List<Order> getPendingOrders() {
        return pendingOrders;
    }
    public Map<Stock, List<Order>> getSellOrders() {
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

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Stocklist2 stocklist = new Stocklist2();
        Price2 price2 = new Price2();
        PriorityQueue<Stock> retrievedStockList = stocklist.fetchStockList();
        TradingEngine tradingEngine = new TradingEngine(new ArrayList<>(retrievedStockList));
//        tradingEngine.getStocklist();
        TradingApp ta= new TradingApp(tradingEngine);
        Order.Type buy = Order.Type.BUY;
        Order.Type sell = Order.Type.SELL;
        Order.Position market = Order.Position.MARKET;
        Order.Position limit = Order.Position.LIMIT;
        Stock stock = new Stock("4715.KL");
        Order order = new Order(stock, buy, market, 500);
        Order order1 = new Order(stock, sell, market, 100);
        User user = new User("LiChee", "lichee03@gmail.com", "12345");
        System.out.println(order.toString());
        ta.placeOrder(user,order);
        System.out.println("Name: "+ user.getName()+"\nEmail: "+user.getEmail()+"\nCurrent Holdings : "+user.getPortfolio().getHoldings() +"\nAcoount Balance: "+user.getPortfolio().getAccountBalance());
        ta.placeOrder(user,order1);
        System.out.println("Name: "+ user.getName()+"\nEmail: "+user.getEmail()+"\nCurrent Holdings : "+user.getPortfolio().getHoldings() +"\nAcoount Balance: "+user.getPortfolio().getAccountBalance());
    }
}

