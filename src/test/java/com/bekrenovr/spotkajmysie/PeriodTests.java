package com.bekrenovr.spotkajmysie;

import com.bekrenovr.spotkajmysie.dto.Period;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PeriodTests {
    @Test
    public void testContains(){
        Period period = new Period(LocalTime.of(8, 0), LocalTime.of(10, 0));

        assertTrue(period.contains(LocalTime.of(9, 0)));
        assertFalse(period.contains(LocalTime.of(8, 0)));
        assertFalse(period.contains(LocalTime.of(10, 0)));
        assertFalse(period.contains(LocalTime.of(11, 0)));
    }

    @Test
    public void testOverlaps_PeriodsIntersect(){
        Period period1 = new Period(LocalTime.of(8, 0), LocalTime.of(10, 0));
        Period period2 = new Period(LocalTime.of(9, 0), LocalTime.of(11, 0));

        assertTrue(period1.overlaps(period2));
        assertTrue(period2.overlaps(period1));
    }

    @Test
    public void testOverlaps_OnePeriodIncludesAnother(){
        Period period1 = new Period(LocalTime.of(8, 0), LocalTime.of(10, 0));
        Period period2 = new Period(LocalTime.of(8, 30), LocalTime.of(9, 30));

        assertTrue(period1.overlaps(period2));
        assertTrue(period2.overlaps(period1));
    }

    @Test
    public void testOverlaps_PeriodsAreEqual(){
        Period period1 = new Period(LocalTime.of(8, 0), LocalTime.of(10, 0));
        Period period2 = new Period(LocalTime.of(8, 0), LocalTime.of(10, 0));

        assertTrue(period1.overlaps(period2));
        assertTrue(period2.overlaps(period1));
    }

    @Test
    public void testOverlaps_NoOverlap(){
        Period period1 = new Period(LocalTime.of(8, 0), LocalTime.of(10, 0));
        Period period2 = new Period(LocalTime.of(10, 0), LocalTime.of(12, 0));

        assertFalse(period1.overlaps(period2));
        assertFalse(period2.overlaps(period1));
    }
}
