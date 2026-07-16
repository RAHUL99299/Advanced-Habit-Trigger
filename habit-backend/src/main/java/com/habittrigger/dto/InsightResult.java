package com.habittrigger.dto;

import java.util.List;

public class InsightResult {

    private Long habitId;
    private String habitName;
    private List<TriggerImpact> impacts;
    private String topInsightText;
    private String summary;

    // Constructors
    public InsightResult() {}

    public InsightResult(Long habitId, String habitName, List<TriggerImpact> impacts, String topInsightText, String summary) {
        this.habitId = habitId;
        this.habitName = habitName;
        this.impacts = impacts;
        this.topInsightText = topInsightText;
        this.summary = summary;
    }

    // Getters and Setters
    public Long getHabitId() { return habitId; }
    public void setHabitId(Long habitId) { this.habitId = habitId; }

    public String getHabitName() { return habitName; }
    public void setHabitName(String habitName) { this.habitName = habitName; }

    public List<TriggerImpact> getImpacts() { return impacts; }
    public void setImpacts(List<TriggerImpact> impacts) { this.impacts = impacts; }

    public String getTopInsightText() { return topInsightText; }
    public void setTopInsightText(String topInsightText) { this.topInsightText = topInsightText; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    // Builder
    public static InsightResultBuilder builder() {
        return new InsightResultBuilder();
    }

    public static class InsightResultBuilder {
        private Long habitId;
        private String habitName;
        private List<TriggerImpact> impacts;
        private String topInsightText;
        private String summary;

        InsightResultBuilder() {}

        public InsightResultBuilder habitId(Long habitId) { this.habitId = habitId; return this; }
        public InsightResultBuilder habitName(String habitName) { this.habitName = habitName; return this; }
        public InsightResultBuilder impacts(List<TriggerImpact> impacts) { this.impacts = impacts; return this; }
        public InsightResultBuilder topInsightText(String topInsightText) { this.topInsightText = topInsightText; return this; }
        public InsightResultBuilder summary(String summary) { this.summary = summary; return this; }

        public InsightResult build() {
            return new InsightResult(habitId, habitName, impacts, topInsightText, summary);
        }
    }

    public static class TriggerImpact {
        private String triggerType;
        private String triggerValue;
        private double impactScore;
        private int sampleSize;
        private String insightText;

        // Constructors
        public TriggerImpact() {}

        public TriggerImpact(String triggerType, String triggerValue, double impactScore, int sampleSize, String insightText) {
            this.triggerType = triggerType;
            this.triggerValue = triggerValue;
            this.impactScore = impactScore;
            this.sampleSize = sampleSize;
            this.insightText = insightText;
        }

        // Getters and Setters
        public String getTriggerType() { return triggerType; }
        public void setTriggerType(String triggerType) { this.triggerType = triggerType; }

        public String getTriggerValue() { return triggerValue; }
        public void setTriggerValue(String triggerValue) { this.triggerValue = triggerValue; }

        public double getImpactScore() { return impactScore; }
        public void setImpactScore(double impactScore) { this.impactScore = impactScore; }

        public int getSampleSize() { return sampleSize; }
        public void setSampleSize(int sampleSize) { this.sampleSize = sampleSize; }

        public String getInsightText() { return insightText; }
        public void setInsightText(String insightText) { this.insightText = insightText; }

        // Builder
        public static TriggerImpactBuilder builder() {
            return new TriggerImpactBuilder();
        }

        public static class TriggerImpactBuilder {
            private String triggerType;
            private String triggerValue;
            private double impactScore;
            private int sampleSize;
            private String insightText;

            TriggerImpactBuilder() {}

            public TriggerImpactBuilder triggerType(String triggerType) { this.triggerType = triggerType; return this; }
            public TriggerImpactBuilder triggerValue(String triggerValue) { this.triggerValue = triggerValue; return this; }
            public TriggerImpactBuilder impactScore(double impactScore) { this.impactScore = impactScore; return this; }
            public TriggerImpactBuilder sampleSize(int sampleSize) { this.sampleSize = sampleSize; return this; }
            public TriggerImpactBuilder insightText(String insightText) { this.insightText = insightText; return this; }

            public TriggerImpact build() {
                return new TriggerImpact(triggerType, triggerValue, impactScore, sampleSize, insightText);
            }
        }
    }
}
