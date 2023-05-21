package com.example.testing1.WallStreet;

public class Stock implements Comparable<Stock> {
    private String symbol;
    private String name;
    private String currency;
    private String exchange;
    private String mic;
    private String country;
    private String type;

    public Stock(String symbol,String name,String currency,String exchange,String mic,String country,String type){
        this.symbol=symbol;
        this.name=name;
        this.currency=currency;
        this.exchange=exchange;
        this.mic=mic;
        this.country=country;
        this.type=type;
    }
    public String getSymbol(){
        return symbol;
    }
    public String getName(){
        return symbol;
    }
    public String getCurrency(){
        return symbol;
    }
    public String getExchange(){
        return exchange;
    }
    public String getMic(){
        return symbol;
    }
    public String getCountry(){
        return symbol;
    }
    public String getType(){
        return symbol;
    }



public String toString(){
        return  "Symbol: "+this.symbol+
                "\nName: "+this.name+
                "\nCurrency: "+this.currency+
                "\nExchange: "+this.exchange+
                "\nMic: "+this.mic+
                "\nCountry: "+this.country+
                "\nType: "+this.type
                +"\n";
}


    @Override
    public int compareTo(Stock o) {
        return symbol.compareTo(o.getSymbol());

    }
}
