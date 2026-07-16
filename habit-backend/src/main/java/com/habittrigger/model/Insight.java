package com.habittrigger.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "insights")
public class Insight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    @Column(name = "trigger_type", length = 30)
    private String triggerType; // MOOD, WEATHER, LOCATION, TIME

    @Column(name = "trigger_value", length = 50)
    private String triggerValue;

    @Column(name = "impact_score", precision = 5, scale = 2)
    private BigDecimal impactScore;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
    }

    // Constructors
    public Insight() {}

    public Insight(Long id, Habit habit, String triggerType, String triggerValue, BigDecimal impactScore, LocalDateTime generatedAt) {
        this.id = id;
        this.habit = habit;
        this.triggerType = triggerType;
        this.triggerValue = triggerValue;
        this.impactScore = impactScore;
        this.generatedAt = generatedAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Habit getHabit() { return habit; }
    public void setHabit(Habit habit) { this.habit = habit; }

    public String getTriggerType() { return triggerType; }
    public void setTriggerType(String triggerType) { this.triggerType = triggerType; }

    public String getTriggerValue() { return triggerValue; }
    public void setTriggerValue(String triggerValue) { this.triggerValue = triggerValue; }

    public BigDecimal getImpactScore() { return impactScore; }
    public void setImpactScore(BigDecimal impactScore) { this.impactScore = impactScore; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

    // Manual Builder Pattern
    public static InsightBuilder builder() {
        return new InsightBuilder();
    }

    public static class InsightBuilder {
        private Long id;
        private Habit habit;
        private String triggerType;
        private String triggerValue;
        private BigDecimal impactScore;
        private LocalDateTime generatedAt;

        InsightBuilder() {}

        public InsightBuilder id(Long id) { this.id = id; return this; }
        public InsightBuilder habit(Habit habit) { this.habit = habit; return this; }
        public InsightBuilder triggerType(String triggerType) { this.triggerType = triggerType; return this; }
        public InsightBuilder triggerValue(String triggerValue) { this.triggerValue = triggerValue; return this; }
        public InsightBuilder impactScore(BigDecimal impactScore) { this.impactScore = impactScore; return this; }
        public InsightBuilder generatedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; return this; }

        public Insight build() {
            return new Insight(id, habit, triggerType, triggerValue, impactScore, generatedAt);
        }
    }
}
