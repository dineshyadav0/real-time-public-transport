package com.haryana.roadways.repository;

import com.haryana.roadways.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRepository extends JpaRepository<Bus, Long> {
}