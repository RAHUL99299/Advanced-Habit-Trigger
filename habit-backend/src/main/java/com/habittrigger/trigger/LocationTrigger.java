package com.habittrigger.trigger;

import com.habittrigger.model.HabitLog;

import java.util.List;

/**
 * INHERITANCE: Extends Trigger abstract class.
 * POLYMORPHISM: Implements calculateImpactScore() with location-specific logic.
 *
 * Calculates what percentage of habit logs were completed when the user was
 * at this trigger's location (e.g. "gym", "home").
 */
public class LocationTrigger extends Trigger {

    public LocationTrigger(String value) {
        super("LOCATION", value);
    }

    @Override
    public double calculateImpactScore(List<HabitLog> logs) {
        if (logs == null || logs.isEmpty()) return 0.0;

        long matching = logs.stream()
                .filter(l -> l.getLocation() != null && l.getLocation().equalsIgnoreCase(getValue()))
                .count();

        if (matching == 0) return 0.0;

        long completed = logs.stream()
                .filter(l -> l.getLocation() != null
                        && l.getLocation().equalsIgnoreCase(getValue())
                        && l.isCompleted())
                .count();

        return (double) completed / matching;
    }
}
