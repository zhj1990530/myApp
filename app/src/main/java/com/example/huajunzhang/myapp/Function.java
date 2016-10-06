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

	private static final String OPEN_WEATHER_MAP_URL =
			"http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";

    private static final String OPEN_WEATHER_MAP_URL_2 =
            "http://api.openweathermap.org/data/2.5/forecast/daily?lat=%s&lon=%s&units=metric&cnt=5";
    //http://api.openweathermap.org/data/2.5/forecast/daily?units=metric&cnt=5

	private static final String OPEN_WEATHER_MAP_API = "2cc497cafac898b5b9aae020a2040dc2";





	public interface AsyncResponse {

		//void processFinish(String output1, String[] output2, String output3);
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
            //JSONObject jsonForecast = null;
			try {
				jsonWeather = getWeatherJSON(params[0], params[1]);
                //jsonForecast = getForeastJSON(params[0], params[1]);


            } catch (Exception e) {
				Log.d("Error", "Cannot process JSON results", e);
			}


			return jsonWeather;
		}
         @Override
         protected void onPostExecute(JSONObject json) {
             try {
                 if(json != null){

					 //JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                     JSONObject city = json.getJSONObject("city");

                     //JSONObject list = json.getJSONArray("list");
					 //System.err.println("json 1");
					 DateFormat df = DateFormat.getDateInstance();
					 //DateFormat df = DateFormat.getDateTimeInstance();
					 //System.err.println("json 2");

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
                             ListItems[i]= temperatureMin+"-"+temperatureMax;
							 String updatedOn = df.format(new Date(day.getLong("dt")*1000));
							 ListItems[i] += " " + updatedOn;
                             //System.out.println(tempForm());
                             //System.out.println(ListItems[i]);

                         } else {
                             temperatureMin = String.format("%.2f", temp.getDouble("min")) + "°C";
                             temperatureMax = String.format("%.2f", temp.getDouble("max")) + "°C";
                             //System.out.println(tempForm());
                             ListItems[i]= temperatureMin+"-"+temperatureMax;
							 String updatedOn = df.format(new Date(day.getLong("dt")*1000));
							 ListItems[i] += " " + updatedOn;
							 //System.out.println(ListItems[i]);

                         }
                     }

					 //String updatedOn = df.format(new Date(json.getLong("dt")*1000));



					 delegate.processFinish(cityName, ListItems);
					 //System.err.println("json 4");


				 }else{
					 System.err.println("json is null");
				 }
             } catch (JSONException e) {
				 System.err.println("json translate error");
				 //Log.e("MainActivity", "Cannot process JSON results", e);
             }
         }
     }
    /*
		@Override
		protected void onPostExecute(JSONObject json) {
			try {
			if(json != null){
				JSONObject details = json.getJSONArray("weather").getJSONObject(0);
				JSONObject main = json.getJSONObject("main");
				DateFormat df = DateFormat.getDateTimeInstance();
				String city = json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country");
                String temperature="no data";
                //call jni coversion C to F
				if(!tempForm()){
					 temperature = String.format("%.2f", toFahrenheit(main.getDouble("temp"))) + "°F";
                     //System.out.println(tempForm());
				}else{
					 temperature = String.format("%.2f", main.getDouble("temp"))+ "°C";
                     //System.out.println(tempForm());

                }

				String updatedOn = df.format(new Date(json.getLong("dt")*1000));


				delegate.processFinish(city, temperature, updatedOn);

			}
			} catch (JSONException e) {
                //Log.e(LOG_TAG, "Cannot process JSON results", e);
            }
		}
	}
*/

	public static JSONObject getWeatherJSON(String lat, String lon){
		try {
			URL url = new URL(String.format(OPEN_WEATHER_MAP_URL_2, lat, lon));
			//URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?lat=32.715736&lon=-117.161100&&units=metric&cnt=5");
			HttpURLConnection connection =
					(HttpURLConnection)url.openConnection();

			connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));

			StringBuffer json = new StringBuffer(1024);
			String tmp="";
			while((tmp=reader.readLine())!=null)
				json.append(tmp).append("\n");
			reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
			// successful
			if(data.getInt("cod") != 200){
				//System.err.println("kkkkkkkkk");
				return null;

			}
			//System.err.println("yyyyyyyyyy");

			return data;
		}catch(Exception e){
			//System.err.println("jjjjjjjjjjjjjj");
			return null;
		}
	}

	public static native  double toFahrenheit(double n1);
    //native class to conversion c->F:)

	public static native  boolean tempForm();
    //return current temperture format

	static {
		System.loadLibrary("native-lib");
	}


}