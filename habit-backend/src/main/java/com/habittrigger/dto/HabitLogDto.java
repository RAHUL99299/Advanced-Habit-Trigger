package com.habittrigger.dto;

import java.time.LocalDate;

public class HabitLogDto {

    private Long id;
    private Long habitId;
    private LocalDate logDate;
    private boolean completed;
    private String mood;
    private String weather;
    private String location;
    private String timeOfDay;
    private String notes;

    // Constructors
    public HabitLogDto() {}

    public HabitLogDto(Long id, Long habitId, LocalDate logDate, boolean completed, String mood, String weather, String location, String timeOfDay, String notes) {
        this.id = id;
        this.habitId = habitId;
        this.logDate = logDate;
        this.completed = completed;
        this.mood = mood;
        this.weather = weather;
        this.location = location;
        this.timeOfDay = timeOfDay;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getHabitId() { return habitId; }
    public void setHabitId(Long habitId) { this.habitId = habitId; }

    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }

    public String getWeather() { return weather; }
    public void setWeather(String weather) { this.weather = weather; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getTimeOfDay() { return timeOfDay; }
    public void setTimeOfDay(String timeOfDay) { this.timeOfDay = timeOfDay; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    // Builder
    public static HabitLogDtoBuilder builder() {
        return new HabitLogDtoBuilder();
    }

    public static class HabitLogDtoBuilder {
        private Long id;
        private Long habitId;
        private LocalDate logDate;
        private boolean completed;
        private String mood;
        private String weather;
        private String location;
        private String timeOfDay;
        private String notes;

        HabitLogDtoBuilder() {}

        public HabitLogDtoBuilder id(Long id) { this.id = id; return this; }
        public HabitLogDtoBuilder habitId(Long habitId) { this.habitId = habitId; return this; }
        public HabitLogDtoBuilder logDate(LocalDate logDate) { this.logDate = logDate; return this; }
        public HabitLogDtoBuilder completed(boolean completed) { this.completed = completed; return this; }
        public HabitLogDtoBuilder mood(String mood) { this.mood = mood; return this; }
        public HabitLogDtoBuilder weather(String weather) { this.weather = weather; return this; }
        public HabitLogDtoBuilder location(String location) { this.location = location; return this; }
        public HabitLogDtoBuilder timeOfDay(String timeOfDay) { this.timeOfDay = timeOfDay; return this; }
        public HabitLogDtoBuilder notes(String notes) { this.notes = notes; return this; }

        public HabitLogDto build() {
            return new HabitLogDto(id, habitId, logDate, completed, mood, weather, location, timeOfDay, notes);
        }
    }
}
