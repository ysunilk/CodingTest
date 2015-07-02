
package com.tcs.lib;

import android.content.Context;


import com.tcs.lib.forecast.IWProvider;
import com.tcs.lib.model.CurrentWeather;
import com.tcs.lib.request.WeatherRequest;


public abstract class WClient {


    private static WClient me;
    protected IWProvider provider;
    protected Context ctx;

    // location time out in sec
    public static int LOCATION_TIMEOUT = 5;


    // Init method
    public void init(Context ctx) {
        this.ctx = ctx;
    }


    public abstract void getWeatherCondition(String location, final WeatherEventListener listener) ;

    public abstract void getWeatherCondition(WeatherRequest request, final WeatherEventListener listener) ;

    public abstract void setProvider();

    public static interface WeatherClientListener {

      //Connection error
        public void onConnectionError(Throwable t);
    }

    public static interface WeatherEventListener extends WeatherClientListener {


        public void onWeatherRetrieved(CurrentWeather weather);
    }


}
