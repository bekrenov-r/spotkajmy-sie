package com.bekrenovr.spotkajmysie.dto;

import com.bekrenovr.spotkajmysie.validation.PeriodConstraint;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CalendarDTO(
        @JsonProperty("working_hours")
        @NotNull
        @PeriodConstraint
        Period workingHours,
        @JsonProperty("planned_meeting")
        @NotNull
        List<@PeriodConstraint Period> plannedMeetings
) { }
