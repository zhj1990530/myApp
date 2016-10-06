package com.example.huajunzhang.myapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    //create new sensor manger
    private SensorManager mgr;
    private Sensor temp;

    //create textviews
    TextView cityField, currentTemperatureField, updatedField, day1_view,day2_view, day3_view,
            day4_view, day5_view,description ;

    //initiallize switcher temp format
    boolean switcher=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //create main active
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //connect to sensor service
        mgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        //start sensor type = ambiant temperature
        temp = mgr.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        //connect textview with xml id
        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);

        toggle.setText("to Fahrenheit");
        // Sets the text for when the button is first created.

        toggle.setTextOff("to Fahrenheit");
        // Sets the text for when the button is not in the checked state.

        toggle.setTextOn("to Celsius");
        // Sets the text for when the button is in the checked state.

        currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
        cityField = (TextView)findViewById(R.id.city_field);
        updatedField = (TextView)findViewById(R.id.updated_field);
        description =(TextView)findViewById(R.id.description);
        day1_view = (TextView)findViewById(R.id.day1);
        day2_view = (TextView)findViewById(R.id.day2);
        day3_view = (TextView)findViewById(R.id.day3);
        day4_view = (TextView)findViewById(R.id.day4);
        day5_view = (TextView)findViewById(R.id.day5);


        description.setText("Next 5 days temperature");
        /*create toggle listener
        * on click switch from c to F OR F  to c
        * */
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    switcher=false;
                    //call native methods to switch temp format
                    switchForm(switcher);
                    Function.placeIdTask asyncTask =new Function.placeIdTask(new Function.AsyncResponse() {
                        public void processFinish(String weather_city, String[] weather_temperature) {
                            //display text
                            cityField.setText(weather_city);
                            day1_view.setText(weather_temperature[0]);
                            day2_view.setText(weather_temperature[1]);
                            day3_view.setText(weather_temperature[2]);
                            day4_view.setText(weather_temperature[3]);
                            day5_view.setText(weather_temperature[4]);


                        }
                    });
                    asyncTask.execute("32.715736", "-117.161100");
                    //asyncTask.execute("Latitude", "Longitude")
                } else {
                    switcher=true;
                    //call native methods to switch temp format
                    switchForm(switcher);
                    Function.placeIdTask asyncTask =new Function.placeIdTask(new Function.AsyncResponse() {
                        public void processFinish(String weather_city, String[] weather_temperature) {
                            //display text
                            cityField.setText(weather_city);
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
                //display text
                cityField.setText(weather_city);
                day1_view.setText(weather_temperature[0]);
                day2_view.setText(weather_temperature[1]);
                day3_view.setText(weather_temperature[2]);
                day4_view.setText(weather_temperature[3]);
                day5_view.setText(weather_temperature[4]);

            }
        });
        asyncTask.execute("32.715736", "-117.161100");
        //asyncTask.execute("Latitude", "Longitude")


    }

    //declare native methods switch temp format
    public static native  boolean switchForm(boolean CorF );

    //connect to native library
    static {
        System.loadLibrary("native-lib");
    }

    /*overide abstract methods in sensor event listener
    * */
    @Override
    protected void onResume() {
        //resume sensor
        mgr.registerListener(this, temp, SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //stop sensor
        mgr.unregisterListener(this, temp);
        super.onPause();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //nothing to do
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        String temperature;
        //convert temperature to require format
        if (!Function.tempForm()) {
            //call native methos from function convert c to f
            temperature = String.format("%.2f", Function.toFahrenheit(event.values[0])) + "°F";

        } else {
            temperature = String.format("%.2f", event.values[0]) + "°C";

        }
        //display text
        currentTemperatureField.setText(temperature);

    }


}
