package com.haryana.roadways.repository;

import com.haryana.roadways.entity.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteStopRepository extends JpaRepository<RouteStop, Long> {

    List<RouteStop> findByRouteIdOrderByStopOrderAsc(Long routeId);

    List<RouteStop> findByStopId(Long stopId);
}