package com.habittrigger.repository;

import com.habittrigger.model.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {
    List<HabitLog> findByHabitIdOrderByLogDateDesc(Long habitId);
    List<HabitLog> findByHabitId(Long habitId);

    @Query("SELECT hl FROM HabitLog hl WHERE hl.habit.id = :habitId AND hl.logDate BETWEEN :startDate AND :endDate ORDER BY hl.logDate DESC")
    List<HabitLog> findByHabitIdAndDateRange(
        @Param("habitId") Long habitId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    boolean existsByHabitIdAndLogDate(Long habitId, LocalDate logDate);

    @Query("SELECT COUNT(hl) FROM HabitLog hl WHERE hl.habit.user.id = :userId AND hl.logDate = :date AND hl.completed = true")
    long countCompletedByUserAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}
