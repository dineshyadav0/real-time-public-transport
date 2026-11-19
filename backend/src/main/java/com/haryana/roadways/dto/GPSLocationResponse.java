
package com.haryana.roadways.dto;

import com.haryana.roadways.entity.GPSLocation;
import java.time.LocalDateTime;

public class GPSLocationResponse {

    private Long id;
    private Long tripId;
    private Double latitude;
    private Double longitude;
    private Double speed;
    private LocalDateTime recordedAt;

    public GPSLocationResponse(GPSLocation location) {
        this.id = location.getId();
        this.tripId = location.getTrip().getId();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.speed = location.getSpeed();
        this.recordedAt = location.getRecordedAt();
    }

    public Long getId() {
        return id;
    }

    public Long getTripId() {
        return tripId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getSpeed() {
        return speed;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }
}
