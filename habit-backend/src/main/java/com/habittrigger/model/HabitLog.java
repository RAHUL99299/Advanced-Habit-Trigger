package com.habittrigger.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "habit_logs")
public class HabitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(nullable = false)
    private boolean completed;

    @Column(length = 30)
    private String mood; // happy, calm, stressed, tired, motivated

    @Column(length = 30)
    private String weather; // sunny, cloudy, rainy, snowy, windy

    @Column(length = 50)
    private String location; // home, office, gym, outdoors, other

    @Column(name = "time_of_day", length = 20)
    private String timeOfDay; // morning, afternoon, evening, night

    @Column(length = 255)
    private String notes;

    // Constructors
    public HabitLog() {}

    public HabitLog(Long id, Habit habit, LocalDate logDate, boolean completed, String mood, String weather, String location, String timeOfDay, String notes) {
        this.id = id;
        this.habit = habit;
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

    public Habit getHabit() { return habit; }
    public void setHabit(Habit habit) { this.habit = habit; }

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

    // Manual Builder Pattern
    public static HabitLogBuilder builder() {
        return new HabitLogBuilder();
    }

    public static class HabitLogBuilder {
        private Long id;
        private Habit habit;
        private LocalDate logDate;
        private boolean completed;
        private String mood;
        private String weather;
        private String location;
        private String timeOfDay;
        private String notes;

        HabitLogBuilder() {}

        public HabitLogBuilder id(Long id) { this.id = id; return this; }
        public HabitLogBuilder habit(Habit habit) { this.habit = habit; return this; }
        public HabitLogBuilder logDate(LocalDate logDate) { this.logDate = logDate; return this; }
        public HabitLogBuilder completed(boolean completed) { this.completed = completed; return this; }
        public HabitLogBuilder mood(String mood) { this.mood = mood; return this; }
        public HabitLogBuilder weather(String weather) { this.weather = weather; return this; }
        public HabitLogBuilder location(String location) { this.location = location; return this; }
        public HabitLogBuilder timeOfDay(String timeOfDay) { this.timeOfDay = timeOfDay; return this; }
        public HabitLogBuilder notes(String notes) { this.notes = notes; return this; }

        public HabitLog build() {
            return new HabitLog(id, habit, logDate, completed, mood, weather, location, timeOfDay, notes);
        }
    }
}
