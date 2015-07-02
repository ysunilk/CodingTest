
package com.tcs.lib;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;


import com.tcs.lib.forecast.ForecastIOWProvider;
import com.tcs.lib.model.CurrentWeather;
import com.tcs.lib.request.WeatherRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class StandardHttpClient extends WClient {

    public static int LOCATION_TIMEOUT = 5;

    // weather provider

    private static StandardHttpClient me;


    public static StandardHttpClient getInstance() {
        if (me == null)
            me = new StandardHttpClient();

        return me;
    }

    //Init context
    @Override
    public void init(Context ctx) {
        super.init(ctx);
    }


    @Override
    public void getWeatherCondition(String location, WeatherEventListener listener)  {
        getWeatherCondition(new WeatherRequest(location), listener);
    }


    @Override
    public void getWeatherCondition(WeatherRequest request, WeatherEventListener listener)  {
        String url = provider.getCurrentWeatherURL(request);
        String output = null;

        try {
            output = connectAndReadData(url);
        } catch (Throwable t) {
            listener.onConnectionError(t);
            return;
        }


        CurrentWeather weather = provider.getWeatherCondition(output);
        listener.onWeatherRetrieved(weather);


    }


    private String connectAndReadData(String url) throws Throwable {
        HttpURLConnection connection = null;
        StringBuffer buffer = new StringBuffer();
        try {

            connection = (HttpURLConnection) (new URL(url)).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\r\n");
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            try {
                connection.disconnect();
            } catch (Throwable t) {
            }
        }

        return buffer.toString();
    }

    private LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    public void setProvider(){
        provider = new ForecastIOWProvider();
    }

}
