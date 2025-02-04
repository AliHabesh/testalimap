package com.example.testalimap;

public class Events {

    private String eventType;
    private double longitude;
    private double latitude;
    private String description;
    private String capacity;

    public Events() {
    }

    public Events(String eventType, double longitude, double latitude, String description, String capacity) {
        this.eventType = eventType;
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = description;
        this.capacity = capacity;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Events{" +
                "eventType='" + eventType + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", description='" + description + '\'' +
                ", capacity='" + capacity + '\'' +
                '}';
    }
}
