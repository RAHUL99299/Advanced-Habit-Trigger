package com.habittrigger.service;

import com.habittrigger.dto.HabitLogDto;
import com.habittrigger.exception.ResourceNotFoundException;
import com.habittrigger.model.Habit;
import com.habittrigger.model.HabitLog;
import com.habittrigger.model.User;
import com.habittrigger.repository.HabitLogRepository;
import com.habittrigger.repository.HabitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HabitLogService {

    private final HabitLogRepository habitLogRepository;
    private final HabitRepository habitRepository;

    public HabitLogService(HabitLogRepository habitLogRepository, HabitRepository habitRepository) {
        this.habitLogRepository = habitLogRepository;
        this.habitRepository = habitRepository;
    }

    public List<HabitLogDto> getLogsForHabit(Long habitId, User user) {
        // Verify habit belongs to user
        habitRepository.findByIdAndUserId(habitId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Habit not found: " + habitId));

        return habitLogRepository.findByHabitIdOrderByLogDateDesc(habitId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public HabitLogDto createLog(Long habitId, HabitLogDto dto, User user) {
        Habit habit = habitRepository.findByIdAndUserId(habitId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Habit not found: " + habitId));

        HabitLog log = HabitLog.builder()
                .habit(habit)
                .logDate(dto.getLogDate())
                .completed(dto.isCompleted())
                .mood(dto.getMood())
                .weather(dto.getWeather())
                .location(dto.getLocation())
                .timeOfDay(dto.getTimeOfDay())
                .notes(dto.getNotes())
                .build();

        HabitLog saved = habitLogRepository.save(log);
        return toDto(saved);
    }

    @Transactional
    public HabitLogDto updateLog(Long habitId, Long logId, HabitLogDto dto, User user) {
        habitRepository.findByIdAndUserId(habitId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Habit not found: " + habitId));

        HabitLog log = habitLogRepository.findById(logId)
                .orElseThrow(() -> new ResourceNotFoundException("Log not found: " + logId));

        log.setCompleted(dto.isCompleted());
        if (dto.getMood() != null) log.setMood(dto.getMood());
        if (dto.getWeather() != null) log.setWeather(dto.getWeather());
        if (dto.getLocation() != null) log.setLocation(dto.getLocation());
        if (dto.getTimeOfDay() != null) log.setTimeOfDay(dto.getTimeOfDay());
        if (dto.getNotes() != null) log.setNotes(dto.getNotes());

        return toDto(habitLogRepository.save(log));
    }

    @Transactional
    public void deleteLog(Long habitId, Long logId, User user) {
        habitRepository.findByIdAndUserId(habitId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Habit not found: " + habitId));
        habitLogRepository.deleteById(logId);
    }

    private HabitLogDto toDto(HabitLog log) {
        return HabitLogDto.builder()
                .id(log.getId())
                .habitId(log.getHabit().getId())
                .logDate(log.getLogDate())
                .completed(log.isCompleted())
                .mood(log.getMood())
                .weather(log.getWeather())
                .location(log.getLocation())
                .timeOfDay(log.getTimeOfDay())
                .notes(log.getNotes())
                .build();
    }
}
