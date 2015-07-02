
package com.tcs.lib.request;


public class WeatherRequest {
    private String cityId;
    private double lon;
    private double lat;

    public WeatherRequest(String cityId) {
        this.cityId = cityId;
    }

    public WeatherRequest(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public String getCityId() {
        return cityId;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }
}
