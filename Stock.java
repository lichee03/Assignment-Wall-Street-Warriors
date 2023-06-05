/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author lichee
 */
public class Stock implements Comparable<Stock> {
    private String symbol;
    private String name;
    private String currency;
    private String exchange;
    private String mic;
    private String country;
    private String type;

    private double price;
    private Price2 price2 ;
    private Stocklist2 stocklist;
    private boolean priceUpdated=false;
    private double currentPrice;
    private int averageVolume;
    private double previousDayClosePrice;
    private int dailyVolume;

    public Stock(String symbol) {
        this.symbol = symbol;
    }
    public Stock(String symbol, String name, String currency, String exchange, String mic, String country, String type) {
        this.symbol = symbol;
        this.name = name;
        this.currency = currency;
        this.exchange = exchange;
        this.mic = mic;
        this.country = country;
        this.type = type;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public String getExchange() {
        return exchange;
    }

    public String getMic() {
        return mic;
    }

    public String getCountry() {
        return country;
    }

    public String getType() {
        return type;
    }



    public String toString() {
        return "Symbol: " + this.symbol +
                "  Name: " + this.name +
                "  Currency: " + this.currency +
                "  Exchange: " + this.exchange +
                "  Mic: " + this.mic +
                "  Country: " + this.country +
                "  Type: " + this.type+ "\n";
    }


    @Override
    public int compareTo(Stock o) {
        return symbol.compareTo(o.getSymbol());

    }


    public double getPrice() {
        if(!priceUpdated){
            updatePrice();
            return price;
        }
        return price;
    }

    public boolean updatePrice() {
        this.price2= new Price2();
        price2.fetchData(getSymbol());
        this.price=price2.getPrices();
        return priceUpdated=true;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public int getAverageVolume() {
        return averageVolume;
    }

    public double getPreviousDayClosePrice() {
        return previousDayClosePrice;
    }

    public int getDailyVolume() {
        return dailyVolume;
    }
}
