package com.example.hhharsh.near;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

/**
 * Created by hhharsh on 4/1/18.
 */

public class QueryUtils {


    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private QueryUtils() {
    }

    private static URL createUrl(String stringUrl){
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream=null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");

            urlConnection.connect();
            Log.v("one","connection done");
            if(urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
                Log.v("Connected to internet?","Yes,fetching data");
            } else {
                Log.e("aaa", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line!=null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<Item> fetchData(String requestUrl){
        Log.v("FirstUrl",requestUrl);
        //Create URL object
        URL url = createUrl(requestUrl);

        //Perform http request to the url and receive json response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        //Extract relevant fields from JSON response and creat list of earthquakes
        List<Item> earthquake = extractFeatureFromJson(jsonResponse);
        return earthquake;
    }

    public static ArrayList<String> fetchLocation(String requestUrl){

        //Create URL object
        URL url = createUrl(requestUrl);

        //Perform http request to the url and receive json response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        //Extract relevant fields from JSON response and creat list of earthquakes
        ArrayList<String> place = extractFeatureFromJson2(jsonResponse);
        return place;
    }

    public static Double fetchRating(String requestUrl){


        //Create URL object
        URL url = createUrl(requestUrl);

        //Perform http request to the url and receive json response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        //Extract relevant fields from JSON response and creat list of earthquakes
        Double ra = extractFeatureFromJson3(jsonResponse);

        return ra;
    }


    private static List<Item> extractFeatureFromJson(String jsonResponse){


        String address="";
        String url="";
        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        List<Item> earthquakes = new ArrayList<>();

        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject jsonObj = new JSONObject(jsonResponse);
            JSONObject response = jsonObj.getJSONObject("response");
            JSONArray ven=response.getJSONArray("venues");

            for(int i=0; i<ven.length();i++)
            {
                JSONObject place = ven.getJSONObject(i);
               String loc_name=place.getString("name");

               JSONObject loc_obj=place.getJSONObject("location");

               if( loc_obj.has("address")) {

                   address = loc_obj.getString("address");
               }





                JSONObject url_obj=place.getJSONObject("stats");

               if(url_obj.has("url")) {
                   url = url_obj.getString("url");
               }


                String place_id=place.getString("id");


String aa="https://api.foursquare.com/v2/venues/";
String bb="?v=20160607&client_id=HCVIWM1P3CR3SNBVL4FRMQWJ2TLKVU55ZLDVOZLWKOAO3ZDY&client_secret=OUAK3RINU4UZNRMS4NBBR3R2O0EB4I10QMEENEWFLWGVJFDG";
StringBuilder sb3=new StringBuilder(aa);
sb3.append(place_id);
sb3.append(bb);
Log.v("rating_url",sb3.toString());
Double rat=fetchRating(sb3.toString());

earthquakes.add(new Item(loc_name,address,rat,url));

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }


    private static ArrayList<String> extractFeatureFromJson2(String jsonResponse){
        String city="";
        String state="";
        String country="";
        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        ArrayList<String> arr = new ArrayList<>();

        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject jsonObj = new JSONObject(jsonResponse);
            JSONObject response = jsonObj.getJSONObject("response");
            JSONArray ven=response.getJSONArray("venues");


                JSONObject place = ven.getJSONObject(0);


                JSONObject loc_obj=place.getJSONObject("location");


                if(loc_obj.has("city")) {
                     city = loc_obj.getString("city");
                }

                if(loc_obj.has("state")) {
                     state = loc_obj.getString("state");
                }

                if(loc_obj.has("country")) {
                     country = loc_obj.getString("country");

                }


                arr=new ArrayList<>();
                arr.add( city);
                arr.add(state);
                arr.add(country);


                 return arr;


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return arr;
    }


    private static Double extractFeatureFromJson3(String jsonResponse){
        ArrayList<String> arr=null;
        JSONObject ven=null;
        JSONObject ven2=null;
        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        Double rr=0.0;

        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject jsonObj = new JSONObject(jsonResponse);
            JSONObject response = jsonObj.getJSONObject("response");
            if(response.has("venues")) {
                ven = response.getJSONObject("venues");
                if(ven.has("rating")) {
                    rr = ven.getDouble("rating");

                }
            }

            else if(response.has("venue")){
              ven2 = response.getJSONObject("venue");
                if(ven2.has("rating")) {
                    rr = ven2.getDouble("rating");

                }
            }




            return rr;

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return rr;
    }

}
