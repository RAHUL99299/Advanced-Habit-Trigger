package com.habittrigger.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "habits")
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 50)
    private String category;

    @Column(name = "target_frequency", length = 20)
    private String targetFrequency; // daily, weekly

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HabitLog> logs;

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Insight> insights;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public Habit() {}

    public Habit(Long id, User user, String name, String category, String targetFrequency, LocalDateTime createdAt, List<HabitLog> logs, List<Insight> insights) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.category = category;
        this.targetFrequency = targetFrequency;
        this.createdAt = createdAt;
        this.logs = logs;
        this.insights = insights;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTargetFrequency() { return targetFrequency; }
    public void setTargetFrequency(String targetFrequency) { this.targetFrequency = targetFrequency; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<HabitLog> getLogs() { return logs; }
    public void setLogs(List<HabitLog> logs) { this.logs = logs; }

    public List<Insight> getInsights() { return insights; }
    public void setInsights(List<Insight> insights) { this.insights = insights; }

    // Manual Builder Pattern
    public static HabitBuilder builder() {
        return new HabitBuilder();
    }

    public static class HabitBuilder {
        private Long id;
        private User user;
        private String name;
        private String category;
        private String targetFrequency;
        private LocalDateTime createdAt;
        private List<HabitLog> logs;
        private List<Insight> insights;

        HabitBuilder() {}

        public HabitBuilder id(Long id) { this.id = id; return this; }
        public HabitBuilder user(User user) { this.user = user; return this; }
        public HabitBuilder name(String name) { this.name = name; return this; }
        public HabitBuilder category(String category) { this.category = category; return this; }
        public HabitBuilder targetFrequency(String targetFrequency) { this.targetFrequency = targetFrequency; return this; }
        public HabitBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public HabitBuilder logs(List<HabitLog> logs) { this.logs = logs; return this; }
        public HabitBuilder insights(List<Insight> insights) { this.insights = insights; return this; }

        public Habit build() {
            return new Habit(id, user, name, category, targetFrequency, createdAt, logs, insights);
        }
    }
}
