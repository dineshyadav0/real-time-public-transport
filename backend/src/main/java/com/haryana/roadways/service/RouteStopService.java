
package com.haryana.roadways.service;

import com.haryana.roadways.entity.BusStop;
import com.haryana.roadways.entity.Route;
import com.haryana.roadways.entity.RouteStop;
import com.haryana.roadways.repository.BusStopRepository;
import com.haryana.roadways.repository.RouteRepository;
import com.haryana.roadways.repository.RouteStopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteStopService {

    private final RouteStopRepository routeStopRepository;
    private final RouteRepository routeRepository;
    private final BusStopRepository busStopRepository;

    public RouteStopService(
            RouteStopRepository routeStopRepository,
            RouteRepository routeRepository,
            BusStopRepository busStopRepository) {
        this.routeStopRepository = routeStopRepository;
        this.routeRepository = routeRepository;
        this.busStopRepository = busStopRepository;
    }

    public RouteStop addStopToRoute(
            Long routeId, Long stopId, Integer stopOrder) {

        if (stopOrder == null || stopOrder < 1) {
            throw new IllegalArgumentException(
                    "Stop order must be at least 1");
        }

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() ->
                        new RuntimeException("Route not found: " + routeId));

        BusStop stop = busStopRepository.findById(stopId)
                .orElseThrow(() ->
                        new RuntimeException("Bus stop not found: " + stopId));

        RouteStop routeStop = new RouteStop();
        routeStop.setRoute(route);
        routeStop.setStop(stop);
        routeStop.setStopOrder(stopOrder);

        return routeStopRepository.save(routeStop);
    }

    public List<RouteStop> getStopsByRoute(Long routeId) {
        if (!routeRepository.existsById(routeId)) {
            throw new RuntimeException("Route not found: " + routeId);
        }

        return routeStopRepository
                .findByRouteIdOrderByStopOrderAsc(routeId);
    }

    public List<RouteStop> getRoutesByStop(Long stopId) {
        if (!busStopRepository.existsById(stopId)) {
            throw new RuntimeException("Bus stop not found: " + stopId);
        }

        return routeStopRepository.findByStopId(stopId);
    }

    public void removeStopFromRoute(Long id) {
        if (!routeStopRepository.existsById(id)) {
            throw new RuntimeException("Route stop not found: " + id);
        }

        routeStopRepository.deleteById(id);
    }
}
