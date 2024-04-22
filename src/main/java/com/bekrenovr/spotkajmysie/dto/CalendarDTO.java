package com.bekrenovr.spotkajmysie.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;
import java.util.List;

public record CalendarDTO(
        @JsonProperty("working_hours")
        Period workingHours,
        @JsonProperty("planned_meeting")
        List<Period> workingMeetings
) { }
