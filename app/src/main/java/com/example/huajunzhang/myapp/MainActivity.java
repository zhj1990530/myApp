package com.example.huajunzhang.myapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import java.util.*;
import android.view.View;
import android.widget.AdapterView;

import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
public class MainActivity extends AppCompatActivity implements SensorEventListener{

    //start new sensor manger
    private SensorManager mgr;
    private Sensor temp;
    private StringBuilder msg = new StringBuilder(2048);

    static String[] values = new String[5];
    TextView cityField, currentTemperatureField, updatedField, day1_view,day2_view, day3_view, day4_view, day5_view,description ;
    //ListView listView ;
    //Typeface weatherFont;

    boolean switcher=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        mgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);

        temp = mgr.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
        //currentTemperatureField.setIncludeFontPadding(false);
        currentTemperatureField.setText("aaaa");
        currentTemperatureField.setText("bbbb");
        //weatherFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/weathericons-regular-webfont.ttf");
        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);

        toggle.setText("to Fahrenheit");
        // Sets the text for when the button is first created.

        toggle.setTextOff("to Fahrenheit");
        // Sets the text for when the button is not in the checked state.

        toggle.setTextOn("to Celsius");
        // Sets the text for when the button is in the checked state.

        cityField = (TextView)findViewById(R.id.city_field);
        updatedField = (TextView)findViewById(R.id.updated_field);
        description =(TextView)findViewById(R.id.description);
        day1_view = (TextView)findViewById(R.id.day1);
        day2_view = (TextView)findViewById(R.id.day2);
        day3_view = (TextView)findViewById(R.id.day3);
        day4_view = (TextView)findViewById(R.id.day4);
        day5_view = (TextView)findViewById(R.id.day5);
        //currentTemperatureField.setText("Sensor Data");

        //detailsField = (TextView)findViewById(R.id.details_field);
        //humidity_field = (TextView)findViewById(R.id.humidity_field);
        //pressure_field = (TextView)findViewById(R.id.pressure_field);
        //weatherIcon = (TextView)findViewById(R.id.weather_icon);
        //weatherIcon.setTypeface(weatherFont);
        description.setText("Next 5 days temperature");
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switcher=false;
                    switchForm(switcher);
                    Function.placeIdTask asyncTask =new Function.placeIdTask(new Function.AsyncResponse() {
                        public void processFinish(String weather_city, String[] weather_temperature) {

                            //public void processFinish(String weather_city, String weather_temperature,  String weather_updatedOn) {

                            cityField.setText(weather_city);
                            //updatedField.setText(weather_updatedOn);
                            //values=weather_temperature;

                            day1_view.setText(weather_temperature[0]);
                            day2_view.setText(weather_temperature[1]);
                            day3_view.setText(weather_temperature[2]);
                            day4_view.setText(weather_temperature[3]);
                            day5_view.setText(weather_temperature[4]);


                        }
                    });
                    asyncTask.execute("32.715736", "-117.161100"); //  asyncTask.execute("Latitude", "Longitude")
                } else {
                    switcher=true;
                    switchForm(switcher);
                    Function.placeIdTask asyncTask =new Function.placeIdTask(new Function.AsyncResponse() {
                        public void processFinish(String weather_city, String[] weather_temperature) {
                            //public void processFinish(String weather_city, String weather_temperature, String weather_updatedOn) {

                            cityField.setText(weather_city);
                            //updatedField.setText(weather_updatedOn);
                            values=weather_temperature;
                            //currentTemperatureField.setText(weather_temperature);
                            //weatherIcon.setText(Html.fromHtml(weather_iconText));
                            //System.out.println(values);
                            day1_view.setText(weather_temperature[0]);
                            day2_view.setText(weather_temperature[1]);
                            day3_view.setText(weather_temperature[2]);
                            day4_view.setText(weather_temperature[3]);
                            day5_view.setText(weather_temperature[4]);

                        }
                    });
                    asyncTask.execute("32.715736", "-117.161100"); //  asyncTask.execute("Latitude", "Longitude")

                }
            }
        });

        Function.placeIdTask asyncTask =new Function.placeIdTask(new Function.AsyncResponse() {
            public void processFinish(String weather_city, String[] weather_temperature) {
                //public void processFinish(String weather_city, String weather_temperature, String weather_updatedOn) {

                cityField.setText(weather_city);
                //updatedField.setText(weather_updatedOn);
                //detailsField.setText(weather_description);
                day1_view.setText(weather_temperature[0]);
                day2_view.setText(weather_temperature[1]);
                day3_view.setText(weather_temperature[2]);
                day4_view.setText(weather_temperature[3]);
                day5_view.setText(weather_temperature[4]);
                //values=weather_temperature;
                //currentTemperatureField.setText(weather_temperature);

                //System.out.println(values);

            }
        });
        asyncTask.execute("32.715736", "-117.161100"); //  asyncTask.execute("Latitude", "Longitude")


        /*listView = (ListView) findViewById(R.id.list);

        // Assign adapter to ListView


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,values);
        //private ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();
        //listView.setAdapter(adapter);
        */

    }

    public static native  boolean switchForm(boolean CorF );

    /* This is another native method declaration that is *not*
     * implemented by 'hello-jni'. This is simply to show that
     * you can declare as many native methods in your Java code
     * as you want, their implementation is searched in the
     * currently loaded native libraries only the first time
     * you call them.
     *
     * Trying to call this function will result in a
     * java.lang.UnsatisfiedLinkError exception !
     */

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onResume() {
        mgr.registerListener(this, temp, SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mgr.unregisterListener(this, temp);
        super.onPause();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        String temperature;
        if (!Function.tempForm()) {
            temperature = String.format("%.2f", Function.toFahrenheit(event.values[0])) + "°F";

        } else {
            temperature = String.format("%.2f", event.values[0]) + "°C";

        }
        //msg.insert(0,  event.values[0]);
        //currentTemperatureField.setText(msg);
        //currentTemperatureField.invalidate();
        currentTemperatureField.setText(temperature);

    }


}
