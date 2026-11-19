package com.haryana.roadways.repository;

import com.haryana.roadways.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {
}