
package com.tcs.lib.forecast;

import com.tcs.lib.model.CurrentWeather;
import com.tcs.lib.request.WeatherRequest;


public interface IWProvider {

    public CurrentWeather getWeatherCondition(String data) ;
    public String getCurrentWeatherURL(WeatherRequest request);


}
