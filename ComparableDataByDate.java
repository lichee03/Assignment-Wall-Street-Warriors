package com.example.testing1.WallStreet;

//class to compare and sort the date in prio queue by ascending order of the vlaue of data

public class ComparableDataByDate implements Comparable<ComparableDataByDate> {
    private String date;
    private double data;
    public ComparableDataByDate(String date,double data){
        this.date=date;
        this.data=data;
    }
    public double getData() {
        return data;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int compareTo(ComparableDataByDate o) {
        return this.getDate().compareTo(o.getDate());
    }

    public String toString(){
        return "Date: "+this.date+"  Price(RM): " + this.data;
    }
}

