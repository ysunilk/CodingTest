package com.tcs.weatherapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.tcs.lib.StandardHttpClient;
import com.tcs.lib.WClient;
import com.tcs.lib.model.CurrentWeather;
import com.tcs.lib.model.Weather;
import com.tcs.lib.request.WeatherRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WeatherActivity extends Activity {

    private WClient client;
    private TextView condDescr;
    private TextView temp;
    private TextView sunrise;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        temp = (TextView) findViewById(R.id.temp);
        condDescr = (TextView) findViewById(R.id.descrWeather);
        sunrise = (TextView) findViewById(R.id.sunrise);
        client = new StandardHttpClient();
        client.setProvider();

    }

    @Override
    public void onStart() {
        super.onStart();

        new WeatherTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    public static String convertDate(long unixTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(unixTime * 1000);
        sdf.setTimeZone(cal.getTimeZone());
        return sdf.format(cal.getTime());
    }

    class WeatherTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            client.getWeatherCondition(new WeatherRequest(51.50F, -0.12F), new WClient.WeatherEventListener() {
                @Override
                public void onWeatherRetrieved(final CurrentWeather cWeather) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Weather weather = cWeather.weather;

                            temp.setText("Temperature:  " + weather.temperature.getTemp());
                            condDescr.setText("Description: " + weather.currentCondition.getDescr());
                            sunrise.setText("Sunrise :  " + convertDate(weather.location.getSunrise()));


                        }
                    });

                }


                @Override
                public void onConnectionError(Throwable t) {

                }
            });
            return null;
        };


    }
}
