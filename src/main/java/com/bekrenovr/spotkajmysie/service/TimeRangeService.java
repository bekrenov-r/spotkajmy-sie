package com.bekrenovr.spotkajmysie.service;

import com.bekrenovr.spotkajmysie.dto.CalendarDTO;
import com.bekrenovr.spotkajmysie.dto.Period;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TimeRangeService {

    public List<List<LocalTime>> generateTimeRanges(List<CalendarDTO> calendars, String meetingDuration) {
        if(calendars.size() < 2)
            throw new RuntimeException("At least two calendars are expected");
        long meetingDurationMinutes = parseStrToDurationMinutes(meetingDuration);
        List<Period> possibleMeetings = calendars.stream()
                .map(calendar -> getPossibleMeetingsFromCalendar(calendar, meetingDurationMinutes))
                .reduce((prevList, nextList) -> prevList.stream()
                        .filter(nextList::contains)
                        .collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
        return joinAdjacentPeriods(possibleMeetings)
                .map(m -> List.of(m.start(), m.end()))
                .toList();
    }

    private Stream<Period> joinAdjacentPeriods(List<Period> periods) {
        for(int i=0; i < periods.size()-1; i++){
            Period current = periods.get(i);
            Period next = periods.get(i+1);
            if(current.end().equals(next.start())){
                periods.set(i, null);
                periods.set(i+1, new Period(current.start(), next.end()));
            }
        }
        return periods.stream().filter(Objects::nonNull);
    }

    private List<Period> getPossibleMeetingsFromCalendar(CalendarDTO calendar, long meetingDurationMinutes){
        LocalTime workingHoursStart = calendar.workingHours().start();
        LocalTime workingHoursEnd = calendar.workingHours().end();
        return Stream.iterate(workingHoursStart,
                        time -> time.plusMinutes(meetingDurationMinutes).isBefore(workingHoursEnd)
                                || time.plusMinutes(meetingDurationMinutes).equals(workingHoursEnd),
                        time -> time.plusMinutes(meetingDurationMinutes))
                .map(time -> new Period(time, time.plusMinutes(meetingDurationMinutes)))
                .filter(period -> calendar.plannedMeetings().stream().noneMatch(period::overlaps))
                .collect(Collectors.toList());
    }

    private long parseStrToDurationMinutes(String str){
        long hours = Long.parseLong(str.split(":")[0]);
        long minutes = Long.parseLong(str.split(":")[1]);
        return hours*60+minutes;
    }
}
