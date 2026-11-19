package com.haryana.roadways.service; // Apne project ka package name check kar lena

public class EtaRequest {
    private double distance_remaining;
    private double average_speed;
    private int traffic_level;
    private int hour;
    private int day_of_week;
    private int stops_remaining;

    public EtaRequest(double distance_remaining, double average_speed, int traffic_level, int hour, int day_of_week, int stops_remaining) {
        this.distance_remaining = distance_remaining;
        this.average_speed = average_speed;
        this.traffic_level = traffic_level;
        this.hour = hour;
        this.day_of_week = day_of_week;
        this.stops_remaining = stops_remaining;
    }

    // Getters and Setters
    public double getDistance_remaining() { return distance_remaining; }
    public void setDistance_remaining(double distance_remaining) { this.distance_remaining = distance_remaining; }

    public double getAverage_speed() { return average_speed; }
    public void setAverage_speed(double average_speed) { this.average_speed = average_speed; }

    public int getTraffic_level() { return traffic_level; }
    public void setTraffic_level(int traffic_level) { this.traffic_level = traffic_level; }

    public int getHour() { return hour; }
    public void setHour(int hour) { this.hour = hour; }

    public int getDay_of_week() { return day_of_week; }
    public void setDay_of_week(int day_of_week) { this.day_of_week = day_of_week; }

    public int getStops_remaining() { return stops_remaining; }
    public void setStops_remaining(int stops_remaining) { this.stops_remaining = stops_remaining; }
}