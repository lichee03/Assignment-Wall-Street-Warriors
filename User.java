/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dsgroup;

/**
 *
 * @author sharr
 */
public class User {

    private String id;
    private double accountBalance;
    private double totalPnL;
    private int numTrades;
    private int winningTrades;
    private int losingTrades;

    // Constructor
    public User(String id, double accountBalance) {
        this.id = id;
        this.accountBalance = accountBalance;
        this.totalPnL = 0.0;
        this.numTrades = 0;
        this.winningTrades = 0;
        this.losingTrades = 0;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public double getTotalPnL() {
        return totalPnL;
    }

    public void updateTotalPnL(double pnl) {
        this.totalPnL += pnl;
    }

    public int getNumTrades() {
        return numTrades;
    }

    public void incrementNumTrades() {
        this.numTrades++;
    }

    public int getWinningTrades() {
        return winningTrades;
    }

    public void incrementWinningTrades() {
        this.winningTrades++;
    }

    public int getLosingTrades() {
        return losingTrades;
    }

    public void incrementLosingTrades() {
        this.losingTrades++;
    }
}
