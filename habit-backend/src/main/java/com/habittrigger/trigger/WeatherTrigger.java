package com.habittrigger.trigger;

import com.habittrigger.model.HabitLog;

import java.util.List;

/**
 * INHERITANCE: Extends Trigger abstract class.
 * POLYMORPHISM: Implements calculateImpactScore() with weather-specific logic.
 *
 * Calculates what percentage of habit logs were completed when the weather
 * matched this trigger's value (e.g. "rainy").
 */
public class WeatherTrigger extends Trigger {

    public WeatherTrigger(String value) {
        super("WEATHER", value);
    }

    @Override
    public double calculateImpactScore(List<HabitLog> logs) {
        if (logs == null || logs.isEmpty()) return 0.0;

        long matching = logs.stream()
                .filter(l -> l.getWeather() != null && l.getWeather().equalsIgnoreCase(getValue()))
                .count();

        if (matching == 0) return 0.0;

        long completed = logs.stream()
                .filter(l -> l.getWeather() != null
                        && l.getWeather().equalsIgnoreCase(getValue())
                        && l.isCompleted())
                .count();

        return (double) completed / matching;
    }
}
