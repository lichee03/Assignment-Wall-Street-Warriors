public class User {

    public class User {
        private String name;
        private String email;
        private String password;
        private PortFolio portfolio;
        private boolean disqualified=false;

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
        public void Disqualified(boolean disqualified){
            disqualified=disqualified;
        }
        public boolean CheckDisqualified(){
            return disqualified;
        }
    }
}
