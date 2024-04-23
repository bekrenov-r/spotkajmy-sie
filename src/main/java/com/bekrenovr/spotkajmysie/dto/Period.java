package com.bekrenovr.spotkajmysie.dto;

import java.time.LocalTime;

public record Period(LocalTime start, LocalTime end) {
    public boolean overlaps(Period other){
        if(start.equals(other.start) && end.equals(other.end)){
            return true;
        }
        return this.contains(other.start) || this.contains(other.end)
                || other.contains(start) || other.contains(end);
    }

    /**
     * Returns false if given LocalTime equals either to start or end time of period. <br>
     * For example, period 10:00 - 12:00 does not contain LocalTime 10:00 and 12:00
     * */
    public boolean contains(LocalTime time){
        return time.isAfter(start) && time.isBefore(end);
    }
}
