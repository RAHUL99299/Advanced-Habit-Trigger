package com.habittrigger.repository;

import com.habittrigger.model.Insight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface InsightRepository extends JpaRepository<Insight, Long> {
    List<Insight> findByHabitIdOrderByImpactScoreDesc(Long habitId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Insight i WHERE i.habit.id = :habitId")
    void deleteByHabitId(@Param("habitId") Long habitId);
}
