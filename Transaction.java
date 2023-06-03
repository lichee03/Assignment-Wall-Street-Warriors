/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dsgroup;

/**
 *
 * @author sharr
 */
public class Transaction {

    private User user;
    private Order order;
    private Stock stock;
    private Order.Type type;
    private int shares;

    public Transaction(User user, Order order) {
        this.user = user;
        this.order = order;
        this.stock = order.getStock();
        this.type = order.getType();
        this.shares = order.getShares();
    }

    public User getUser() {
        return user;
    }

    public Order getOrder() {
        return order;
    }

    public Stock getStock() {
        return stock;
    }

    public Order.Type getType() {
        return type;
    }

    public int getShares() {
        return shares;
    }

}
