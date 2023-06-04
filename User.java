import java.util.*;
public class User {


        private String name;
        private String email;
        private String password;
        private PortFolio portfolio;
        private boolean disqualified = false;
        private double startingAccountBalance;
        private double currentAccountBalance;
        public double points;

        private Queue<Order> transactionHistory; 

        public User(String name, String email, String password) {
            this.name = name;
            this.email = email; 
            this.password = password;
            this.portfolio = new PortFolio();
            points = 0;
            transactionHistory = new LinkedList<>();
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public PortFolio getPortfolio() {
            return portfolio;
        }

        public void Disqualified(boolean disqualified) {
            disqualified = true;
        }

        public boolean CheckDisqualified() {
            return disqualified;
        }
        public Queue<Order> getTransactionHistory() {
            return transactionHistory;
        }
        
        public double getCurrentAccountBalance() {
                return currentAccountBalance;
        }
        
    }


