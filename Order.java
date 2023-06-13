import ch.qos.logback.core.util.CachingDateFormatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lichee
 */
public class Order {

    public enum Type {
        BUY,
        SELL
    }


    public enum Position {
        MARKET,
        LIMIT
    }

    private Stock stock;
    private Type type;
    private Position position;
    private int shares;
    private double price;
    private int id;
    private String name;
    private LocalDateTime time;

    public Order(int id,Stock stock, Type type, Position position, int shares, double price) {
        this.id=id;
        this.stock = stock;
        this.type = type;
        this.position = position;
        this.shares = shares;
        this.price = price;
        this.time = LocalDateTime.now();
    }

    public Order(int id,Stock stock, Type type, Position position, int shares) {
        this.id=id;
        this.stock = stock;
        this.type = type;
        this.position = position;
        this.shares = shares;
        this.price = getStock().getPrice();
        this.time = LocalDateTime.now();
    }

    public Order(int did, String name,Stock stock, Type type, Position position, int dshare, double dprice, LocalDateTime dateTime) {
        this.id = did;
        this.name=name;
        this.stock = stock;
        this.type = type;
        this.position = position;
        this.shares = dshare;
        this.price = dprice;
        this.time = dateTime;
    }

    public Stock getStock() {
        return stock;
    }

    public Type getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public int getShares() {
        return shares;
    }
    public int getID() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public double getValue() {
        return price * shares;
    }

    public String getTime() {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String formattedtime = time.format(myFormatObj);
        return formattedtime;
    }

    public String toString() {
        String format = "%-10s %-25s %-20s %-15s %-15s %-15s %-15s %-15s";
        String header = String.format(format,"OrderID", "Time", "Stock symbol","Type", "Position", "Price", "Shares","Total price");
        String row = String.format(format, getID(),getTime(), getStock().getSymbol(),getType(), getPosition(), getPrice(), getShares(), getValue());

        StringBuilder table = new StringBuilder();
        table.append("Order submitted.\n");
        table.append("------------------------------------------------------------------------------------------------------------------------------------------------------------------").append("\n");
        table.append(header).append("\n");
        table.append("------------------------------------------------------------------------------------------------------------------------------------------------------------------").append("\n");
        table.append(row).append("\n");

        return table.toString();
    }
}


