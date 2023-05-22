package com.example.testing1.WallStreet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.PriorityQueue;

public class Stocklist2 {
    private PriorityQueue<Stock> stockList;

    public Stocklist2() {
        stockList = new PriorityQueue<>();
    }

    public PriorityQueue<Stock> fetchStockList() {
        try {
            String apiKey = "?apikey=UM-5db9edd90046daabace072a7d8f3a954b799f1cf2a87d04c50cd60eff8d7b846";
            String baseURL = "https://wall-street-warriors-api-um.vercel.app/";
            String whatToFind = "mylist";

            URL url = new URL(baseURL + whatToFind + apiKey);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                StringBuilder informationString = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    informationString.append(line);
                }
                br.close();

                try {
                    String jsonString = informationString.toString();
                    parseStockList(jsonString);
                  //  printStockList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stockList;
    }

    private void parseStockList(String jsonString) {
        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject stock = jsonArray.getJSONObject(i);
            String symbol = stock.getString("symbol");
            String name = stock.getString("name");
            String currency = stock.getString("currency");
            String exchange = stock.getString("exchange");
            String micCode = stock.getString("mic_code");
            String country = stock.getString("country");
            String type = stock.getString("type");

            stockList.offer(new Stock(symbol, name, currency, exchange, micCode, country, type));
        }
    }

    public void printStockList() {
        PriorityQueue<Stock> tempQueue = new PriorityQueue<>(stockList);

        while (!tempQueue.isEmpty()) {
            Stock stock = tempQueue.poll();
            System.out.println(stock);
        }
    }

    public static void main(String[] args) {
        Stocklist2 stocklist = new Stocklist2();
        PriorityQueue<Stock> retrievedStockList = stocklist.fetchStockList();
        stocklist.printStockList();
        // Use the retrievedStockList as needed
    }
}
