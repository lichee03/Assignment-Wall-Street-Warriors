package com.example.testing1.WallStreet;



import org.json.simple.parser.JSONParser;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class WSWAPI {
    public static void main(String[] args) {
        Scanner sc =new Scanner(System.in);
        System.out.print("Symbol: ");
        String symbol=sc.nextLine();
        String symbolUrl= "&symbol="+symbol;

        //prio queue for each data sorted by date in ascending order
        PriorityQueue<ComparableDataByDate> openDate = new PriorityQueue<>();
        PriorityQueue<ComparableDataByDate> highDate = new PriorityQueue<>();
        PriorityQueue<ComparableDataByDate> lowDate = new PriorityQueue<>();
        PriorityQueue<ComparableDataByDate> closeDate = new PriorityQueue<>();
        PriorityQueue<ComparableDataByDate> volumeDate = new PriorityQueue<>();
        PriorityQueue<ComparableDataByDate> dividendsDate = new PriorityQueue<>();
        PriorityQueue<ComparableDataByDate> ssDate = new PriorityQueue<>();


        //prio queue for each data sorted by the price in ascending order
        PriorityQueue<ComparableData> openData = new PriorityQueue<>();
        PriorityQueue<ComparableData> highData = new PriorityQueue<>();
        PriorityQueue<ComparableData> lowData = new PriorityQueue<>();
        PriorityQueue<ComparableData> closeData = new PriorityQueue<>();
        PriorityQueue<ComparableData> volumeData = new PriorityQueue<>();
        PriorityQueue<ComparableData> dividendsData = new PriorityQueue<>();
        PriorityQueue<ComparableData> ssData = new PriorityQueue<>();

        //for refreshing
        Timer timer = new Timer();
        TimerTask task = new TimerTask(){
            @Override
            public void run() {
               clearQueues(openDate,highDate,lowDate,closeDate,volumeDate,dividendsDate,ssDate);
               clearQueues(openData,highData,lowData,closeData,volumeData,dividendsData,ssData);

                try {

                    //Public API:
                    //https://wall-street-warriors-api-um.vercel.app/
                    //api key=79f211cb173045a4bb097f799b364312
                    String apiKey = "?apikey=UM-5db9edd90046daabace072a7d8f3a954b799f1cf2a87d04c50cd60eff8d7b846";
                    String baseURL ="https://wall-street-warriors-api-um.vercel.app/";
                    String whatTofind = "price";

                    URL url = new URL(baseURL+whatTofind+apiKey+symbolUrl);

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


                            //open
                            getData(symbol,"Open",openDate,openData,jsonString);
                            // System.out.println("Open: ");
                            printQueue(openDate);
                            // printQueue(openData);

                            //high
                            getData(symbol,"High",highDate,highData,jsonString);
                            // System.out.println("High: ");
                            //printQueue(highDate);
                            //printQueue(highData);

                            //low
                            getData(symbol,"Low",lowDate,lowData,jsonString);
                            //System.out.println("Low: ");
                            //printQueue(lowDate);
                            // printQueue(lowData);

                            //Close
                            getData(symbol,"Close",closeDate,closeData,jsonString);
                            //System.out.println("Close: ");
                            //printQueue(closeDate);
                            //printQueue(closeData);

                            //Volume
                            getData(symbol,"Volume",volumeDate,volumeData,jsonString);
                            //System.out.println("Volume: ");
                            // printQueue(volumeDate);
                            //  printQueue(volumeData);

                            //Dividens
                            getData(symbol,"Dividends",dividendsDate,dividendsData,jsonString);
                            // System.out.println("Dividends: ");
                            //printQueue(dividendsDate);
                            //printQueue(dividendsData);

                            //Stock Splits
                            getData(symbol,"Stock Splits",ssDate,ssData,jsonString);
                            //System.out.println("Stock Splits: ");
                            //printQueue(ssDate);
                            //printQueue(ssData);

                           // System.out.println();
                            //System.out.println();
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
    static void  getData(String symbol, String dataWant, PriorityQueue<ComparableDataByDate> date, PriorityQueue<ComparableData> data,String jsonString){
        JSONObject json = new JSONObject(jsonString);
        JSONObject jsonObj = json.getJSONObject(symbol).getJSONObject(dataWant);
        for (Object time: jsonObj.keySet()){
            double value = jsonObj.getDouble(time.toString());
            EpochTimestampConverter eptc= new EpochTimestampConverter(time);
            date.offer(new ComparableDataByDate(eptc.convertToDate(),value));
            data.offer(new ComparableData(eptc.convertToDate(), value));
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

    public static <E>void clearQueues(PriorityQueue<E> q1,PriorityQueue<E> q2,PriorityQueue<E> q3,PriorityQueue<E> q4,PriorityQueue<E> q5,PriorityQueue<E> q6,PriorityQueue<E> q7){
        q1.clear();
        q2.clear();
        q3.clear();
        q4.clear();
        q5.clear();
        q6.clear();
        q7.clear();

    }
}
