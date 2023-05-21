/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author lichee
 */
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
    public boolean Disqualified(){
       disqualified=true;
    }
    public boolean CheckDisqualified(){
        return disqualified;
    }



    
}
