
public class User {


        private String name;
        private String email;
        private String password;
        private PortFolio portfolio;
        private boolean disqualified = false;

        public User(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
            this.portfolio = new PortFolio();
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
            disqualified = disqualified;
        }

        public boolean CheckDisqualified() {
            return disqualified;
        }

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


