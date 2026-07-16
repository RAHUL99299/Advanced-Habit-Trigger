package com.habittrigger.service;

import com.habittrigger.dto.HabitDto;
import com.habittrigger.exception.ResourceNotFoundException;
import com.habittrigger.jdbc.AnalyticsJdbcDao;
import com.habittrigger.model.Habit;
import com.habittrigger.model.User;
import com.habittrigger.repository.HabitLogRepository;
import com.habittrigger.repository.HabitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HabitService {

    private final HabitRepository habitRepository;
    private final HabitLogRepository habitLogRepository;
    private final AnalyticsJdbcDao analyticsJdbcDao;

    public HabitService(HabitRepository habitRepository, HabitLogRepository habitLogRepository, AnalyticsJdbcDao analyticsJdbcDao) {
        this.habitRepository = habitRepository;
        this.habitLogRepository = habitLogRepository;
        this.analyticsJdbcDao = analyticsJdbcDao;
    }

    public List<HabitDto> getHabitsForUser(User user) {
        List<Habit> habits = habitRepository.findByUserId(user.getId());
        return habits.stream()
                .map(habit -> enrichHabitDto(habit))
                .collect(Collectors.toList());
    }

    public HabitDto getHabitById(Long habitId, User user) {
        Habit habit = habitRepository.findByIdAndUserId(habitId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Habit not found with id: " + habitId));
        return enrichHabitDto(habit);
    }

    @Transactional
    public HabitDto createHabit(HabitDto dto, User user) {
        Habit habit = Habit.builder()
                .user(user)
                .name(dto.getName())
                .category(dto.getCategory())
                .targetFrequency(dto.getTargetFrequency())
                .build();

        Habit saved = habitRepository.save(habit);
        return toDto(saved);
    }

    @Transactional
    public HabitDto updateHabit(Long habitId, HabitDto dto, User user) {
        Habit habit = habitRepository.findByIdAndUserId(habitId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Habit not found with id: " + habitId));

        habit.setName(dto.getName());
        if (dto.getCategory() != null) habit.setCategory(dto.getCategory());
        if (dto.getTargetFrequency() != null) habit.setTargetFrequency(dto.getTargetFrequency());

        return enrichHabitDto(habitRepository.save(habit));
    }

    @Transactional
    public void deleteHabit(Long habitId, User user) {
        Habit habit = habitRepository.findByIdAndUserId(habitId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Habit not found with id: " + habitId));
        habitRepository.delete(habit);
    }

    private HabitDto enrichHabitDto(Habit habit) {
        HabitDto dto = toDto(habit);

        // Call raw JDBC for streak data
        Map<String, Integer> streakData = analyticsJdbcDao.getStreakData(habit.getId());
        dto.setCurrentStreak(streakData.getOrDefault("currentStreak", 0));
        dto.setLongestStreak(streakData.getOrDefault("longestStreak", 0));

        // JPA for counts
        List<com.habittrigger.model.HabitLog> logs = habitLogRepository.findByHabitId(habit.getId());
        dto.setTotalLogs(logs.size());
        long completed = logs.stream().filter(l -> l.isCompleted()).count();
        dto.setCompletedLogs((int) completed);
        dto.setCompletionRate(logs.isEmpty() ? 0 : Math.round((double) completed / logs.size() * 1000.0) / 10.0);

        return dto;
    }

    private HabitDto toDto(Habit habit) {
        return HabitDto.builder()
                .id(habit.getId())
                .name(habit.getName())
                .category(habit.getCategory())
                .targetFrequency(habit.getTargetFrequency())
                .createdAt(habit.getCreatedAt())
                .build();
    }
}
