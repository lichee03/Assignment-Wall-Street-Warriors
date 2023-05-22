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

    public User(String id, double accountBalance, double totalPnL, int numTrades, int winningTrades, int losingTrades) {
        this.id = id;
        this.accountBalance = accountBalance;
        this.totalPnL = totalPnL;
        this.numTrades = numTrades;
        this.winningTrades = winningTrades;
        this.losingTrades = losingTrades;
    }

    public String getId() {
        return id;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public double getTotalPnL() {
        return totalPnL;
    }

    public int getNumTrades() {
        return numTrades;
    }

    public int getWinningTrades() {
        return winningTrades;
    }

    public int getLosingTrades() {
        return losingTrades;
    }
}

