package com.bekrenovr.spotkajmysie.service;

import com.bekrenovr.spotkajmysie.dto.CalendarDTO;
import com.bekrenovr.spotkajmysie.dto.Period;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TimeRangeService {

    public List<List<LocalTime>> generateTimeRanges(List<CalendarDTO> calendars, String meetingDuration) {
        long meetingDurationMinutes = parseStrToDurationMinutes(meetingDuration);
        List<Period> possibleMeetings1 = getPossibleMeetingsFromCalendar(calendars.get(0), meetingDurationMinutes);
        List<Period> possibleMeetings2 = getPossibleMeetingsFromCalendar(calendars.get(1), meetingDurationMinutes);
        possibleMeetings1.retainAll(possibleMeetings2);
        return joinAdjacentPeriods(possibleMeetings1)
                .map(m -> List.of(m.start(), m.end()))
                .toList();
    }

    public Stream<Period> joinAdjacentPeriods(List<Period> periods) {
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

    public List<Period> getPossibleMeetingsFromCalendar(CalendarDTO calendar, long meetingDurationMinutes){
        List<Period> workingMeetings = calendar.workingMeetings();
        LocalTime workingHoursStart = calendar.workingHours().start();
        LocalTime workingHoursEnd = calendar.workingHours().end();
        return Stream.iterate(workingHoursStart,
                        time -> time.plusMinutes(meetingDurationMinutes).isBefore(workingHoursEnd)
                                || time.plusMinutes(meetingDurationMinutes).equals(workingHoursEnd),
                        time -> time.plusMinutes(meetingDurationMinutes))
                .map(time -> new Period(time, time.plusMinutes(meetingDurationMinutes)))
                .filter(m -> workingMeetings.stream().noneMatch(m::overlaps))
                .collect(Collectors.toList());
    }

    public long parseStrToDurationMinutes(String str){
        long hours = Long.parseLong(str.split(":")[0]);
        long minutes = Long.parseLong(str.split(":")[1]);
        return hours*60+minutes;
    }
}
