
package com.haryana.roadways.repository;

import com.haryana.roadways.entity.BusSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BusScheduleRepository
        extends JpaRepository<BusSchedule, Long> {

    List<BusSchedule> findByRouteId(Long routeId);

    List<BusSchedule> findByDepotNameIgnoreCase(String depotName);

    List<BusSchedule> findByRouteIdOrderByDepartureTimeAsc(Long routeId);

    List<BusSchedule> findByDepartureTimeBetweenOrderByDepartureTimeAsc(
            LocalTime startTime,
            LocalTime endTime
    );

    List<BusSchedule> findByVerifiedOrderByDepartureTimeAsc(
            Boolean verified
    );

    boolean existsByRouteIdAndDepotNameIgnoreCaseAndDepartureTimeAndRouteHeadingAndActualDestinationAndViaAndServiceTypeAndSourcePage(
            Long routeId,
            String depotName,
            LocalTime departureTime,
            String routeHeading,
            String actualDestination,
            String via,
            String serviceType,
            Integer sourcePage
    );

    @Query("""
        SELECT s FROM BusSchedule s
        JOIN s.route r
        WHERE LOWER(r.source) = LOWER(:source)
        AND LOWER(r.destination) = LOWER(:destination)
        AND (s.effectiveFrom IS NULL OR s.effectiveFrom <= :travelDate)
        AND (s.effectiveUntil IS NULL OR s.effectiveUntil >= :travelDate)
        ORDER BY s.departureTime ASC
        """)
    List<BusSchedule> searchDirectSchedules(
            @Param("source") String source,
            @Param("destination") String destination,
            @Param("travelDate") LocalDate travelDate
    );
}
