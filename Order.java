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
    private LocalDateTime time;

    public Order(Stock stock, Type type, Position position, int shares, double price) {
        this.stock = stock;
        this.type = type;
        this.position = position;
        this.shares = shares;
        this.price = price;
        this.time = LocalDateTime.now();
    }

    public Order(Stock stock, Type type, Position position, int shares) {
        this.stock = stock;
        this.type = type;
        this.position = position;
        this.shares = shares;
        this.price = getStock().getPrice();
        this.time = LocalDateTime.now();
    }

    public Order(int did, Stock stock, Type type, Position position, int dshare, double dprice, LocalDateTime dateTime) {
        this.id = did;
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
        return "Order submitted on " + getTime() + "\nType: " + getType() + "\nPosition: " + getPosition() + "\nPrice: " + getPrice() + "\nShare: " + getShares()+"\n";

    }
}



