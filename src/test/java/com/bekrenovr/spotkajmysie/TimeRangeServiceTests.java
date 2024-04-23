package com.bekrenovr.spotkajmysie;

import com.bekrenovr.spotkajmysie.dto.CalendarDTO;
import com.bekrenovr.spotkajmysie.dto.Period;
import com.bekrenovr.spotkajmysie.service.TimeRangeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TimeRangeServiceTests {
    @Autowired
    private TimeRangeService timeRangeService;

    @Test
    public void testGenerateTimeRanges_Basic(){
        CalendarDTO calendar1 = new CalendarDTO(
                new Period(LocalTime.of(10, 0), LocalTime.of(19, 55)),
                List.of(
                        new Period(LocalTime.of(9, 0), LocalTime.of(10,  30)),
                        new Period(LocalTime.of(12, 0), LocalTime.of(13,  0)),
                        new Period(LocalTime.of(16, 0), LocalTime.of(18,  0))
                )
        );
        CalendarDTO calendar2 = new CalendarDTO(
                new Period(LocalTime.of(10, 0), LocalTime.of(18, 30)),
                List.of(
                        new Period(LocalTime.of(10, 0), LocalTime.of(11,  30)),
                        new Period(LocalTime.of(12, 30), LocalTime.of(14,  30)),
                        new Period(LocalTime.of(14, 30), LocalTime.of(15,  0)),
                        new Period(LocalTime.of(16, 0), LocalTime.of(17,  0))
                )
        );
        String meetingDuration = "00:30";

        List<List<LocalTime>> result = timeRangeService.generateTimeRanges(Arrays.asList(calendar1, calendar2), meetingDuration);
        List<List<LocalTime>> expected = List.of(
                List.of(LocalTime.of(11, 30), LocalTime.of(12, 0)),
                List.of(LocalTime.of(15, 0), LocalTime.of(16, 0)),
                List.of(LocalTime.of(18, 0), LocalTime.of(18, 30))
        );

        assertIterableEquals(expected, result);
    }

    @Test
    public void testGenerateTimeRanges_ShouldReturnEmptyList_WhenThereAreNoPossibleMeetings(){
        // works 08:00-12:00
        CalendarDTO calendar1 = new CalendarDTO(
                new Period(LocalTime.of(8, 0), LocalTime.of(12, 0)),
                List.of()
        );

        // works 13:00-20:00
        CalendarDTO calendar2 = new CalendarDTO(
                new Period(LocalTime.of(13, 0), LocalTime.of(20, 0)),
                List.of()
        );
        String meetingDuration = "00:30";

        List<List<LocalTime>> result = timeRangeService.generateTimeRanges(Arrays.asList(calendar1, calendar2), meetingDuration);
        List<List<LocalTime>> expected = List.of();

        assertIterableEquals(expected, result);
    }

    @Test
    public void testGenerateTimeRanges_ShouldThrowRuntimeException_WhenLessThanTwoCalendars(){
        CalendarDTO calendar = new CalendarDTO(null, null);
        String meetingDuration = "00:30";

        Executable exec = () -> timeRangeService.generateTimeRanges(Arrays.asList(calendar), meetingDuration);

        assertThrows(RuntimeException.class, exec);
    }

    @Test
    public void testGenerateTimeRanges_ShouldThrowException_WhenMeetingDurationIsInvalid(){
        String meetingDuration = "asd";

        Executable exec = () -> timeRangeService.generateTimeRanges(Arrays.asList(null, null), meetingDuration);

        assertThrows(NumberFormatException.class, exec);
    }
}
