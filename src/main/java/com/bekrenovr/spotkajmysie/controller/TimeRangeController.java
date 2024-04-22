package com.bekrenovr.spotkajmysie.controller;

import com.bekrenovr.spotkajmysie.dto.CalendarDTO;
import com.bekrenovr.spotkajmysie.service.TimeRangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TimeRangeController {
    private final TimeRangeService timeRangeService;

    @PostMapping("/generate-time-ranges")
    public ResponseEntity<List<List<LocalTime>>> generateTimeRanges(
            @RequestBody List<CalendarDTO> calendars,
            @RequestParam("meeting_duration") String meetingDuration
    ){
        return ResponseEntity.ok(timeRangeService.generateTimeRanges(calendars, meetingDuration));
    }
}
