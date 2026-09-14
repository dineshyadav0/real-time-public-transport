package com.haryana.roadways.service;

import com.haryana.roadways.entity.Bus;
import com.haryana.roadways.repository.BusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusService {

    private final BusRepository busRepository;

    public BusService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    // Create a new bus
    public Bus createBus(Bus bus) {
        return busRepository.save(bus);
    }

    // Get all buses
    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    // Get bus by ID
    public Optional<Bus> getBusById(Long id) {
        return busRepository.findById(id);
    }

    // Update bus
    public Bus updateBus(Long id, Bus updatedBus) {

        Bus existingBus = busRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bus not found with ID: " + id));

        existingBus.setBusNumber(updatedBus.getBusNumber());
        existingBus.setRegistrationNumber(updatedBus.getRegistrationNumber());
        existingBus.setBusType(updatedBus.getBusType());
        existingBus.setCapacity(updatedBus.getCapacity());
        existingBus.setStatus(updatedBus.getStatus());

        return busRepository.save(existingBus);
    }

    // Delete bus
    public void deleteBus(Long id) {

        if (!busRepository.existsById(id)) {
            throw new RuntimeException("Bus not found with ID: " + id);
        }

        busRepository.deleteById(id);
    }
}