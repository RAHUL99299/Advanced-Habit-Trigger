package com.habittrigger.controller;

import com.habittrigger.dto.HabitDto;
import com.habittrigger.model.User;
import com.habittrigger.service.HabitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping
    public ResponseEntity<List<HabitDto>> getHabits(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(habitService.getHabitsForUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitDto> getHabit(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(habitService.getHabitById(id, user));
    }

    @PostMapping
    public ResponseEntity<HabitDto> createHabit(@Valid @RequestBody HabitDto dto, @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(habitService.createHabit(dto, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitDto> updateHabit(@PathVariable Long id,
                                                  @Valid @RequestBody HabitDto dto,
                                                  @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(habitService.updateHabit(id, dto, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id, @AuthenticationPrincipal User user) {
        habitService.deleteHabit(id, user);
        return ResponseEntity.noContent().build();
    }
}
