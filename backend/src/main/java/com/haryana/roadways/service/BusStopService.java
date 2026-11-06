package com.haryana.roadways.service;

import com.haryana.roadways.entity.BusStop;
import com.haryana.roadways.repository.BusStopRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusStopService {

    private final BusStopRepository busStopRepository;

    public BusStopService(BusStopRepository busStopRepository) {
        this.busStopRepository = busStopRepository;
    }

    public BusStop createBusStop(BusStop busStop) {
        return busStopRepository.save(busStop);
    }

    public List<BusStop> getAllBusStops() {
        return busStopRepository.findAll();
    }

    public Optional<BusStop> getBusStopById(Long id) {
        return busStopRepository.findById(id);
    }

    public List<BusStop> getBusStopsByDistrict(String district) {
        return busStopRepository.findByDistrictIgnoreCase(district);
    }

    public BusStop updateBusStop(Long id, BusStop updatedBusStop) {

        BusStop existing = busStopRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Bus stop not found: " + id));

        existing.setName(updatedBusStop.getName());
        existing.setDistrict(updatedBusStop.getDistrict());
        existing.setLatitude(updatedBusStop.getLatitude());
        existing.setLongitude(updatedBusStop.getLongitude());

        return busStopRepository.save(existing);
    }

    public void deleteBusStop(Long id) {

        if (!busStopRepository.existsById(id)) {
            throw new RuntimeException("Bus stop not found: " + id);
        }

        busStopRepository.deleteById(id);
    }
}