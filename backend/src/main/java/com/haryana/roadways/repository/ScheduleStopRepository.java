
package com.haryana.roadways.repository;

import com.haryana.roadways.entity.ScheduleStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ScheduleStopRepository
        extends JpaRepository<ScheduleStop, Long> {

    List<ScheduleStop> findByScheduleIdOrderByStopOrderAsc(
            Long scheduleId);

    List<ScheduleStop> findByStopId(Long stopId);

    @Modifying
    @Query("DELETE FROM ScheduleStop s WHERE s.schedule.id = :scheduleId")
    void deleteByScheduleId(
            @Param("scheduleId") Long scheduleId);

    @Query("""
        SELECT DISTINCT s1.schedule.id
        FROM ScheduleStop s1, ScheduleStop s2
        WHERE s1.schedule.id = s2.schedule.id
        AND s1.stop.id = :sourceStopId
        AND s2.stop.id = :destinationStopId
        AND s1.stopOrder < s2.stopOrder
        ORDER BY s1.schedule.id
        """)
    List<Long> findScheduleIdsBetweenStops(
            @Param("sourceStopId") Long sourceStopId,
            @Param("destinationStopId") Long destinationStopId);
}
