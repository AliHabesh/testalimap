package com.example.testalimap;

public class TestClass {
    private String eventType;
    private double latitude;
    private double longitude;
    private String info;

    public TestClass(String eventType, double latitude, double longitude, String info) {
        this.eventType = eventType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.info = info;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
