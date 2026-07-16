package com.habittrigger.dto;

import java.time.LocalDateTime;

public class HabitDto {

    private Long id;
    private String name;
    private String category;
    private String targetFrequency; // daily, weekly
    private LocalDateTime createdAt;

    // Computed fields
    private int currentStreak;
    private int longestStreak;
    private int totalLogs;
    private int completedLogs;
    private double completionRate;

    // Constructors
    public HabitDto() {}

    public HabitDto(Long id, String name, String category, String targetFrequency, LocalDateTime createdAt, int currentStreak, int longestStreak, int totalLogs, int completedLogs, double completionRate) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.targetFrequency = targetFrequency;
        this.createdAt = createdAt;
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
        this.totalLogs = totalLogs;
        this.completedLogs = completedLogs;
        this.completionRate = completionRate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTargetFrequency() { return targetFrequency; }
    public void setTargetFrequency(String targetFrequency) { this.targetFrequency = targetFrequency; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }

    public int getLongestStreak() { return longestStreak; }
    public void setLongestStreak(int longestStreak) { this.longestStreak = longestStreak; }

    public int getTotalLogs() { return totalLogs; }
    public void setTotalLogs(int totalLogs) { this.totalLogs = totalLogs; }

    public int getCompletedLogs() { return completedLogs; }
    public void setCompletedLogs(int completedLogs) { this.completedLogs = completedLogs; }

    public double getCompletionRate() { return completionRate; }
    public void setCompletionRate(double completionRate) { this.completionRate = completionRate; }

    // Builder
    public static HabitDtoBuilder builder() {
        return new HabitDtoBuilder();
    }

    public static class HabitDtoBuilder {
        private Long id;
        private String name;
        private String category;
        private String targetFrequency;
        private LocalDateTime createdAt;
        private int currentStreak;
        private int longestStreak;
        private int totalLogs;
        private int completedLogs;
        private double completionRate;

        HabitDtoBuilder() {}

        public HabitDtoBuilder id(Long id) { this.id = id; return this; }
        public HabitDtoBuilder name(String name) { this.name = name; return this; }
        public HabitDtoBuilder category(String category) { this.category = category; return this; }
        public HabitDtoBuilder targetFrequency(String targetFrequency) { this.targetFrequency = targetFrequency; return this; }
        public HabitDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public HabitDtoBuilder currentStreak(int currentStreak) { this.currentStreak = currentStreak; return this; }
        public HabitDtoBuilder longestStreak(int longestStreak) { this.longestStreak = longestStreak; return this; }
        public HabitDtoBuilder totalLogs(int totalLogs) { this.totalLogs = totalLogs; return this; }
        public HabitDtoBuilder completedLogs(int completedLogs) { this.completedLogs = completedLogs; return this; }
        public HabitDtoBuilder completionRate(double completionRate) { this.completionRate = completionRate; return this; }

        public HabitDto build() {
            return new HabitDto(id, name, category, targetFrequency, createdAt, currentStreak, longestStreak, totalLogs, completedLogs, completionRate);
        }
    }
}
