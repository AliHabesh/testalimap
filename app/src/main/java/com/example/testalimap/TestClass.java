package com.example.testalimap;

public class TestClass {
    private String eventType;
    private double latitude;
    private double longitude;

    public TestClass(String eventType, double latitude, double longitude) {
        this.eventType = eventType;
        this.latitude = latitude;
        this.longitude = longitude;
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


}
