package com.haryana.roadways.repository;

import com.haryana.roadways.entity.BusStop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusStopRepository extends JpaRepository<BusStop, Long> {

    List<BusStop> findByDistrictIgnoreCase(String district);

    Optional<BusStop> findByNameIgnoreCaseAndDistrictIgnoreCase(
            String name,
            String district
    );
}