
package com.haryana.roadways.service;

import com.haryana.roadways.entity.BusSchedule;
import com.haryana.roadways.repository.BusScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleVerificationService {

    private final BusScheduleRepository repository;

    public ScheduleVerificationService(
            BusScheduleRepository repository) {
        this.repository = repository;
    }

    public List<BusSchedule> getSchedulesByVerification(
            boolean verified) {
        return repository.findByVerifiedOrderByDepartureTimeAsc(
                verified
        );
    }

    @Transactional
    public BusSchedule updateVerification(
            Long scheduleId,
            boolean verified) {

        BusSchedule schedule = repository.findById(scheduleId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Schedule not found: " + scheduleId
                        )
                );

        schedule.setVerified(verified);

        return repository.save(schedule);
    }
}
