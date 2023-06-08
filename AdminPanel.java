/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


public class AdminPanel { //link to User authentification
    private List<User> users;
    private TradingEngine tradingEngine;
    private PortFolio portfolio;
    private Database database= new Database();

    public AdminPanel() {
        users = new ArrayList<>();
    }

    public boolean CheckShortSell(Stock stock, PortFolio portfolio) {
        boolean shortsell = false;
        int currentshares = portfolio.getHoldings().getOrDefault(stock, 0);


        return shortsell;
    }

    public boolean CheckMarginTrade() {
        boolean marginTrade = false;
        //method
        return marginTrade;
    }


    public void disqualifyUser(String username) {
        users=database.retriveUserList();
        for (User user : users) {
            if (user.getName().equals(username)) {
                user.Disqualified();
                break;
            }
        }
    }



    public void checkAccountBalance() {
        users=database.retriveUserList();
        for (User user : users) {
            if (user.getStatus()!="disqualified") {
                portfolio= new PortFolio(user);
                double accountBalance = portfolio.getAccountBalance();
                if (accountBalance >= 0.5 * 500000&& accountBalance<0) {
                    disqualifyUser(user.getName());
                }
            }
        }
    }
}
