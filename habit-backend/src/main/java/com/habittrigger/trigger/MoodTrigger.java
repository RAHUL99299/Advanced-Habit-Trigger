package com.habittrigger.trigger;

import com.habittrigger.model.HabitLog;

import java.util.List;

/**
 * INHERITANCE: Extends Trigger abstract class.
 * POLYMORPHISM: Implements calculateImpactScore() with mood-specific logic.
 *
 * Calculates what percentage of habit logs were completed when the user's
 * mood matched this trigger's value (e.g. "stressed").
 *
 * Note: Mood has a direct emotional influence, so logs with matching mood
 * are counted and completion rate is returned.
 */
public class MoodTrigger extends Trigger {

    public MoodTrigger(String value) {
        super("MOOD", value);
    }

    @Override
    public double calculateImpactScore(List<HabitLog> logs) {
        if (logs == null || logs.isEmpty()) return 0.0;

        long matching = logs.stream()
                .filter(l -> l.getMood() != null && l.getMood().equalsIgnoreCase(getValue()))
                .count();

        if (matching == 0) return 0.0;

        long completed = logs.stream()
                .filter(l -> l.getMood() != null
                        && l.getMood().equalsIgnoreCase(getValue())
                        && l.isCompleted())
                .count();

        return (double) completed / matching;
    }
}
