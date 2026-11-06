
package com.haryana.roadways.controller;

import com.haryana.roadways.entity.RouteStop;
import com.haryana.roadways.service.RouteStopService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/route-stops")
public class RouteStopController {

    private final RouteStopService routeStopService;

    public RouteStopController(RouteStopService routeStopService) {
        this.routeStopService = routeStopService;
    }

    @PostMapping("/{routeId}/stops/{stopId}")
    public RouteStop addStopToRoute(
            @PathVariable Long routeId,
            @PathVariable Long stopId,
            @RequestParam Integer stopOrder) {

        return routeStopService.addStopToRoute(
                routeId, stopId, stopOrder);
    }

    @GetMapping("/{routeId}")
    public List<RouteStop> getStopsByRoute(
            @PathVariable Long routeId) {

        return routeStopService.getStopsByRoute(routeId);
    }

    @GetMapping("/stop/{stopId}")
    public List<RouteStop> getRoutesByStop(
            @PathVariable Long stopId) {

        return routeStopService.getRoutesByStop(stopId);
    }

    @DeleteMapping("/{id}")
    public void removeStopFromRoute(@PathVariable Long id) {
        routeStopService.removeStopFromRoute(id);
    }
}
