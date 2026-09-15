package com.haryana.roadways.service;

import com.haryana.roadways.entity.Route;
import com.haryana.roadways.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public Route createRoute(Route route) {
        return routeRepository.save(route);
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public Optional<Route> getRouteById(Long id) {
        return routeRepository.findById(id);
    }

    public Route updateRoute(Long id, Route updatedRoute) {
        Route existingRoute = routeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found with ID: " + id));

        existingRoute.setRouteNumber(updatedRoute.getRouteNumber());
        existingRoute.setSource(updatedRoute.getSource());
        existingRoute.setDestination(updatedRoute.getDestination());
        existingRoute.setTotalDistance(updatedRoute.getTotalDistance());

        return routeRepository.save(existingRoute);
    }

    public void deleteRoute(Long id) {
        if (!routeRepository.existsById(id)) {
            throw new RuntimeException("Route not found with ID: " + id);
        }

        routeRepository.deleteById(id);
    }
}