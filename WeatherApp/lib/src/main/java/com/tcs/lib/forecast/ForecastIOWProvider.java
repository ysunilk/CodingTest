package com.tcs.lib.forecast;

import android.util.Log;

import com.tcs.lib.model.CurrentWeather;
import com.tcs.lib.model.Weather;
import com.tcs.lib.request.WeatherRequest;

import org.json.JSONException;
import org.json.JSONObject;




public class ForecastIOWProvider implements IWProvider {

    private static final String URL = "https://api.forecast.io/forecast/";
    private static final long EXPIRE_TIME = 5 * 60 * 1000; // 5 min
    private CurrentWeather cWeather;
    private long lastUpdate;
    public String ApiKey = "7378aa0b30f319c61be8003c51ca171f";


    @Override
    public CurrentWeather getWeatherCondition(String data)  {
        if (cWeather != null && !isExpired())
            return cWeather;
        else {
            parseWeatherData(data);
            return cWeather;
        }
    }




    private String createURL(WeatherRequest request) {
        if (ApiKey == null || ApiKey.equals(""))
                return null;

        return URL + ApiKey + "/" + request.getLat() + "," + request.getLon()  ;
    }


    private void parseWeatherData(String data)   {
        lastUpdate = System.currentTimeMillis();

        cWeather = new CurrentWeather();
        Weather weather = new Weather();
        try {
            // Create JSON data
            JSONObject rootObj = new JSONObject(data);

            com.tcs.lib.model.Location loc = new com.tcs.lib.model.Location();
            loc.setLatitude((float)  rootObj.getDouble("latitude"));
            loc.setLongitude((float) rootObj.getDouble("longitude"));

            weather.location = loc;

            // Parse current weather
            JSONObject currently = rootObj.getJSONObject("currently");

            loc.setSunrise(currently.optLong("sunriseTime"));
            loc.setSunset(currently.optLong("sunsetTime"));
            weather = parseWeather(currently);
            cWeather.weather = weather;



        }
        catch (JSONException json) {
            //json.printStackTrace();

        }


        cWeather.weather = weather;
    }

    private Weather parseWeather(JSONObject jsonWeather) throws JSONException {
        Weather weather = new Weather();

        weather.currentCondition.setDescr(jsonWeather.optString("summary"));
        weather.currentCondition.setIcon(jsonWeather.optString("icon"));



        weather.rain[0].setAmmount((float) jsonWeather.optDouble("precipIntensity"));

        weather.rain[0].setChance((float) jsonWeather.optDouble("precipProbability") * 100);

        weather.temperature.setTemp((float) jsonWeather.optDouble("temperature"));
        weather.temperature.setMinTemp((float) jsonWeather.optDouble("temperatureMin"));
        weather.temperature.setMaxTemp((float) jsonWeather.optDouble("temperatureMax"));
        weather.currentCondition.setDewPoint((float) jsonWeather.optDouble("dewPoint"));

        weather.wind.setSpeed((float) jsonWeather.optDouble("windSpeed"));
        weather.wind.setDeg((float) jsonWeather.optDouble("windBearing"));

        weather.clouds.setPerc((int) jsonWeather.optDouble("cloudCover") * 100); // We transform it in percentage
        weather.currentCondition.setHumidity((float) jsonWeather.optDouble("humidity") * 100);
        weather.currentCondition.setVisibility((float) jsonWeather.optDouble("visibility"));
        weather.currentCondition.setPressure((float) jsonWeather.optDouble("pressure"));

        return weather;
    }

    private boolean isExpired() {
        if (lastUpdate == 0)
            return true; // First time;

        if (lastUpdate - System.currentTimeMillis() > EXPIRE_TIME)
            return true;

        return false;
    }

    @Override
    public String getCurrentWeatherURL(WeatherRequest request)  {
        return createURL(request);
    }



}
