/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.time.LocalDateTime;

/**
 *
 * @author lichee
 */
public class Order {

    public enum Type {
        BUY,
        SELL
    }
    public enum Position{
        MARKET,
        PENDING
    }

    private Stock stock; 
    private Type type;
    private Position position;
    private int shares;
    private double price;
    private LocalDateTime time;
    public Order(Stock stock, Type type, Position position ,int shares, double price) {
        this.stock = stock;
        this.type = type;
        this.position=position;
        this.shares = shares;
        this.price = price;
        this.time=LocalDateTime.now();
    }

    public Stock getStock() {
        return stock;
    }

    public Type getType() {
        return type;
    }
    public Position getPosition(){return position;}

    public int getShares() {
        return shares;
    }

    public double getPrice() {
        return price;
    }
    public double getValue(){
        return price*shares;
    }
    public LocalDateTime getTime(){
        return time;
    }
}

