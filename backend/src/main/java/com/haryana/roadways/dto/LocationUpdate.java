package com.haryana.roadways.dto;

public class LocationUpdate {

    private Long tripId;
    private Double latitude;
    private Double longitude;
    private Double speed;
    private String recordedAt;

    public LocationUpdate() {
    }

    public LocationUpdate(
            Long tripId,
            Double latitude,
            Double longitude,
            Double speed,
            String recordedAt) {

        this.tripId = tripId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.recordedAt = recordedAt;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(String recordedAt) {
        this.recordedAt = recordedAt;
    }
}