package com.habittrigger.trigger;

import com.habittrigger.model.HabitLog;

import java.util.List;

/**
 * Abstract base class for all habit triggers.
 *
 * Demonstrates ENCAPSULATION: type and value fields are private, accessed via getters.
 * Demonstrates ABSTRACTION: calculateImpactScore() is abstract — each subclass defines its own logic.
 * Demonstrates INHERITANCE: MoodTrigger, WeatherTrigger, LocationTrigger, TimeTrigger all extend this.
 */
public abstract class Trigger {

    // Encapsulated fields — private, accessed via getters
    private final String type;
    private final String value;

    protected Trigger(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    /**
     * POLYMORPHISM: Each subclass implements this method differently.
     * InsightService calls this on a List<Trigger> — one line of code, four different behaviours.
     *
     * @param logs all habit logs for a given habit
     * @return a score between 0.0 and 1.0 representing completion rate under this trigger
     */
    public abstract double calculateImpactScore(List<HabitLog> logs);

    /**
     * Generates a human-readable insight sentence for this trigger.
     *
     * @param habitName the habit name for context
     * @return a plain-English summary of the impact
     */
    public String generateInsightText(String habitName, double score) {
        int percent = (int) Math.round(score * 100);
        String strength = score >= 0.7 ? "strongly" : score >= 0.5 ? "moderately" : "weakly";
        return String.format(
            "You %s complete '%s' when %s is %s (%d%% completion rate).",
            strength, habitName, type.toLowerCase(), value.toLowerCase(), percent
        );
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[type=" + type + ", value=" + value + "]";
    }
}
