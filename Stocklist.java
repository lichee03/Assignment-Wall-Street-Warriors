package com.example.testing1.WallStreet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
public class Stocklist {

    public static void main(String[] args) {
        Scanner sc =new Scanner(System.in);

        //prio queue for each data sorted by date in ascending order
        PriorityQueue<Stock> stockList = new PriorityQueue<>();


        //for refreshing
        Timer timer = new Timer();
        TimerTask task = new TimerTask(){
            @Override
            public void run() {

                try {

                    //Public API:
                    //https://wall-street-warriors-api-um.vercel.app/
                    //api key=79f211cb173045a4bb097f799b364312
                    String apiKey = "?apikey=UM-5db9edd90046daabace072a7d8f3a954b799f1cf2a87d04c50cd60eff8d7b846";
                    String baseURL ="https://wall-street-warriors-api-um.vercel.app/";
                    String whatTofind = "mylist";

                    URL url = new URL(baseURL+whatTofind+apiKey);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    //Check if connect is made
                    int responseCode = conn.getResponseCode();

                    // 200 OK
                    if (responseCode != 200) {
                        throw new RuntimeException("HttpResponseCode: " + responseCode);
                    } else {
                        //read  data from API and store in  string builder
                        StringBuilder informationString = new StringBuilder();
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        for (String line = br.readLine(); line != null; line = br.readLine()) {
                            informationString.append(line);
                        }
                        br.close();


                        try {
                            String jsonString = informationString.toString();
                            getStockList(stockList,jsonString);

                            printQueue(stockList);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        // timer set to refresh every 5 sec for testing purpose
        timer.scheduleAtFixedRate(task,0,5000);
    }

    // get the data and store in prio queue
    static void  getStockList( PriorityQueue<Stock> stockList, String jsonString) {
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

    public static  <E> void printQueue(PriorityQueue<E> queue) {
        // Create a temporary queue to preserve the original order
        PriorityQueue<E> tempQueue = new PriorityQueue<>(queue);

        // Process each element in the temporary queue
        while (!tempQueue.isEmpty()) {
            E element = tempQueue.poll();
            System.out.println(element);
        }
    }
}
