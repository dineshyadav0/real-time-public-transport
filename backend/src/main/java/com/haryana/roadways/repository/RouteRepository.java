package com.haryana.roadways.repository;

import com.haryana.roadways.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    Optional<Route> findByRouteNumberIgnoreCase(String routeNumber);

    boolean existsByRouteNumberIgnoreCase(String routeNumber);
}