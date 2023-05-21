package com.example.testing1.WallStreet;

//class to compare and sort the data in prio queue by ascending order of the vlaue of data
public class ComparableData implements Comparable<ComparableData> {
    private String date;
    private double data;

    public ComparableData(String date, double data) {
        this.date = date;
        this.data = data;
    }

    public double getData() {
        return data;
    }

    public String getDate() {
        return date;
    }

//    @Override
//    public int compareTo(ComparableData o) {
//        if (this.data < o.data) {
//            return -1;
//        } else if (this.data > o.data) {
//            return 1;
//        } else {
//            return this.date.compareTo(o.date);
//        }
//    }

    public int compareTo(ComparableData o) {
        if (this.data < o.data) {
            return -1;
        } else if (this.data > o.data) {
            return 1;
        } else {
            return this.date.compareTo(o.date);
        }
    }

    public String toString() {
        return "Date: "+this.date+"  Price(RM): " + this.data;
    }
}
