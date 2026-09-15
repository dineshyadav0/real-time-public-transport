package com.haryana.roadways.repository;

import com.haryana.roadways.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}