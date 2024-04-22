package com.bekrenovr.spotkajmysie.dto;

import java.time.Duration;
import java.time.LocalTime;

public record Period(LocalTime start, LocalTime end) {
    public boolean overlaps(Period other){
        if(start.equals(other.start) && end.equals(other.end)){
            return true;
        }
        return this.contains(other.start) || this.contains(other.end)
                || other.contains(start) || other.contains(end);
    }

    public boolean contains(LocalTime time){
        if(start.isAfter(end)){
            return time.isBefore(end) && time.isBefore(start) || time.equals(start) || time.equals(end);
        }
        return time.isAfter(start) && time.isBefore(end);
    }
}
