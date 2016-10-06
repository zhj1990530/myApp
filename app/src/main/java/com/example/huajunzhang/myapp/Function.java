package com.example.huajunzhang.myapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Function {

	//prepare openweather API url

    private static final String OPEN_WEATHER_MAP_URL_2 =
            "http://api.openweathermap.org/data/2.5/forecast/daily?lat=%s&lon=%s&units=metric&cnt=5";

	private static final String OPEN_WEATHER_MAP_API = "2cc497cafac898b5b9aae020a2040dc2";


	//build Async Agruement
	public interface AsyncResponse {

        void processFinish(String output1, String[] output2);

    }

	 public static class placeIdTask extends AsyncTask<String, Void, JSONObject> {

		public AsyncResponse delegate = null;//Call back interface

		 public placeIdTask(AsyncResponse asyncResponse) {
			 delegate = asyncResponse;//Assigning call back interfacethrough constructor
		 }

		@Override
		protected JSONObject doInBackground(String... params) {

			JSONObject jsonWeather = null;
			try {
				//generate json data
				jsonWeather = getWeatherJSON(params[0], params[1]);


            } catch (Exception e) {
				Log.d("Error", "Cannot process JSON results", e);
			}


			return jsonWeather;
		}
		 //json parse function
         @Override
         protected void onPostExecute(JSONObject json) {
             try {
                 if(json != null){

                     JSONObject city = json.getJSONObject("city");

					 DateFormat df = DateFormat.getDateInstance();

					 String cityName = city.getString("name").toUpperCase(Locale.US) + ", " + city.getString("country");

                     String temperatureMin = "no data";
                     String temperatureMax = "no data";
                     String[] ListItems= new String[5];
                     for(int i=0;i<5;i++) {
                         JSONObject day= json.getJSONArray("list").getJSONObject(i);
						 JSONObject temp=day.getJSONObject("temp");
                         //call jni coversion C to F
                         if (!tempForm()) {
                             temperatureMin = String.format("%.2f", toFahrenheit(temp.getDouble("min"))) + "°F";
                             temperatureMax = String.format("%.2f", toFahrenheit(temp.getDouble("max"))) + "°F";
							 String updatedOn = df.format(new Date(day.getLong("dt")*1000));
							 //trasnlate json data to string
							 ListItems[i]= temperatureMin+"-"+temperatureMax;
							 ListItems[i] += " " + updatedOn;
							 //merge string



						 } else {
                             temperatureMin = String.format("%.2f", temp.getDouble("min")) + "°C";
                             temperatureMax = String.format("%.2f", temp.getDouble("max")) + "°C";
							 String updatedOn = df.format(new Date(day.getLong("dt")*1000));
							 //trasnlate json data to string
							 ListItems[i]= temperatureMin+"-"+temperatureMax;
							 ListItems[i] += " " + updatedOn;
							 //merge string


						 }
                     }

					 delegate.processFinish(cityName, ListItems);
					 //send back result

				 }else{
					 System.err.println("json is null");
				 }
             } catch (JSONException e) {
				 System.err.println("json translate error");
				 //Log.e("MainActivity", "Cannot process JSON results", e);
             }
         }
     }


	public static JSONObject getWeatherJSON(String lat, String lon){
		try {
			URL url = new URL(String.format(OPEN_WEATHER_MAP_URL_2, lat, lon));
			HttpURLConnection connection =
					(HttpURLConnection)url.openConnection();
			//open connection
			connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);
			//add api key for verify
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));
			//read json buffer bit by bit
			StringBuffer json = new StringBuffer(1024);
			String tmp="";
			while((tmp=reader.readLine())!=null)
				json.append(tmp).append("\n");
			reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
			// successful
			if(data.getInt("cod") != 200){
				return null;

			}
			return data;
		}catch(Exception e){
			return null;
		}
	}

	public static native  double toFahrenheit(double n1);
    //native class to conversion C->F:)

	public static native  boolean tempForm();
    //return current temperture format

	static {
		System.loadLibrary("native-lib");
	}//connect with native library


}