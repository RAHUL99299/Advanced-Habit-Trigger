package com.habittrigger.controller;

import com.habittrigger.dto.HabitLogDto;
import com.habittrigger.model.User;
import com.habittrigger.service.HabitLogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits/{habitId}/logs")
public class HabitLogController {

    private final HabitLogService habitLogService;

    public HabitLogController(HabitLogService habitLogService) {
        this.habitLogService = habitLogService;
    }

    @GetMapping
    public ResponseEntity<List<HabitLogDto>> getLogs(@PathVariable Long habitId,
                                                      @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(habitLogService.getLogsForHabit(habitId, user));
    }

    @PostMapping
    public ResponseEntity<HabitLogDto> createLog(@PathVariable Long habitId,
                                                   @Valid @RequestBody HabitLogDto dto,
                                                   @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(habitLogService.createLog(habitId, dto, user));
    }

    @PutMapping("/{logId}")
    public ResponseEntity<HabitLogDto> updateLog(@PathVariable Long habitId,
                                                   @PathVariable Long logId,
                                                   @RequestBody HabitLogDto dto,
                                                   @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(habitLogService.updateLog(habitId, logId, dto, user));
    }

    @DeleteMapping("/{logId}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long habitId,
                                           @PathVariable Long logId,
                                           @AuthenticationPrincipal User user) {
        habitLogService.deleteLog(habitId, logId, user);
        return ResponseEntity.noContent().build();
    }
}
