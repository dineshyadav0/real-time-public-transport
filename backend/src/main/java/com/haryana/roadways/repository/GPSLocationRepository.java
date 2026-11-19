package com.haryana.roadways.repository;

import com.haryana.roadways.entity.GPSLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GPSLocationRepository extends JpaRepository<GPSLocation, Long> {

    List<GPSLocation> findByTripId(Long tripId);

    Optional<GPSLocation> findTopByTripIdOrderByRecordedAtDesc(Long tripId);
}