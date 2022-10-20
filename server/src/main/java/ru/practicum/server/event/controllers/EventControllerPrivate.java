package ru.practicum.server.event.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.server.event.services.EventService;

@Slf4j
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventControllerPrivate {
    private final EventService eventService;
    @PostMapping
    @GetMapping
    @PatchMapping
    @GetMapping("/events/{eventId}")
    @PatchMapping("/events/{eventId}")
    @GetMapping(("/events/{eventId}/requests"))
    @PatchMapping("/events/{eventId}/requests/{reqId}/confirm")
    @PatchMapping("/events/{eventId}/requests/{reqId}/reject")
}