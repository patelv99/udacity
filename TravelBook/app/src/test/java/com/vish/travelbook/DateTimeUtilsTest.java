package com.vish.travelbook;

import org.joda.time.DateTime;
import org.junit.Test;

import static com.vish.travelbook.utils.DateTimeUtils.getDatesDuration;
import static com.vish.travelbook.utils.DateTimeUtils.getEventDatesDuration;
import static com.vish.travelbook.utils.DateTimeUtils.getEventNotificationTime;
import static com.vish.travelbook.utils.DateTimeUtils.getNumericalDate;
import static com.vish.travelbook.utils.DateTimeUtils.getNumericalShortDate;
import static com.vish.travelbook.utils.DateTimeUtils.getTextDate;
import static com.vish.travelbook.utils.DateTimeUtils.getTime;
import static com.vish.travelbook.utils.DateTimeUtils.getTimeDuration;
import static com.vish.travelbook.utils.DateTimeUtils.getTripNotificationTime;
import static com.vish.travelbook.utils.DateTimeUtils.parseNumericalShortDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DateTimeUtilsTest {

    private DateTime DEC_31_2017_END = new DateTime().withDate(2017, 12, 31).withTime(23, 59, 59, 0);
    private DateTime JAN_1_2018_START = new DateTime().withDate(2018, 1, 1).withTimeAtStartOfDay();
    private DateTime FEB_4_2018_START = new DateTime().withDate(2018, 2, 4).withTimeAtStartOfDay();
    private DateTime FEB_4_2018_END = new DateTime().withDate(2018, 2, 4).withTime(23, 59, 59, 0);
    private DateTime APR_22_2018_END = new DateTime().withDate(2018, 4, 22).withTime(23, 59, 59, 0);


    @Test
    public void testGetDatesDuration() {
        assertEquals(getDatesDuration(JAN_1_2018_START, FEB_4_2018_END), "Jan 01, 2018 - Feb 04, 2018");
        assertEquals(getDatesDuration(FEB_4_2018_START, FEB_4_2018_END), "Feb 04, 2018");
        assertEquals(getDatesDuration(DEC_31_2017_END, JAN_1_2018_START), "Dec 31, 2017 - Jan 01, 2018");
    }

    @Test
    public void testGetEventDatesDuration() {
        assertEquals(getEventDatesDuration(JAN_1_2018_START, FEB_4_2018_END), "01/01/18 - 02/04/18");
        assertEquals(getEventDatesDuration(FEB_4_2018_START, FEB_4_2018_END), "02/04/18");
        assertEquals(getEventDatesDuration(DEC_31_2017_END, JAN_1_2018_START), "12/31/17 - 01/01/18");
    }

    @Test
    public void testGetNumericalDate() {
        assertEquals(getNumericalDate(JAN_1_2018_START), "01/01/2018");
        assertEquals(getNumericalDate(FEB_4_2018_START), "02/04/2018");
        assertEquals(getNumericalDate(DEC_31_2017_END), "12/31/2017");
    }

    @Test
    public void testGetNumericalShortDate() {
        assertEquals(getNumericalShortDate(JAN_1_2018_START), "01/01/18");
        assertEquals(getNumericalShortDate(FEB_4_2018_START), "02/04/18");
        assertEquals(getNumericalShortDate(DEC_31_2017_END), "12/31/17");
    }

    @Test
    public void testParseNumericalShortDate() {
        assertEquals(parseNumericalShortDate("01/01/18"), JAN_1_2018_START);
        assertEquals(parseNumericalShortDate("02/04/18"), FEB_4_2018_START);
        assertEquals(parseNumericalShortDate("12/31/17"), DEC_31_2017_END.withTimeAtStartOfDay());

        assertNotEquals(parseNumericalShortDate("02/04/18"), FEB_4_2018_END);
    }

    @Test
    public void testGetTextDate() {
        assertEquals(getTextDate(JAN_1_2018_START), "Jan 01, 2018");
        assertEquals(getTextDate(FEB_4_2018_START), "Feb 04, 2018");
        assertEquals(getTextDate(DEC_31_2017_END), "Dec 31, 2017");
    }

    @Test
    public void testGetTimeDuration() {
        assertEquals(getTimeDuration(
                JAN_1_2018_START.withTime(6, 23, 0, 0),
                JAN_1_2018_START.withTime(14, 34, 0, 0)),
                "6:23 AM - 2:34 PM");
        assertEquals(getTimeDuration(FEB_4_2018_START, FEB_4_2018_END), "12:00 AM - 11:59 PM");
    }

    @Test
    public void testGetTime() {
        assertEquals(getTime(JAN_1_2018_START), "12:00 AM");
        assertEquals(getTime(FEB_4_2018_START), "12:00 AM");
        assertEquals(getTime(FEB_4_2018_END), "11:59 PM");
        assertEquals(getTime(DEC_31_2017_END.withTime(17, 23, 9, 0)),
                "5:23 PM");
    }

    @Test
    public void testGetTripNotificationTime() {
        assertEquals(getTripNotificationTime(JAN_1_2018_START), DEC_31_2017_END.withTimeAtStartOfDay());
        assertEquals(getTripNotificationTime(FEB_4_2018_END), FEB_4_2018_END.minusDays(1));
        assertEquals(getTripNotificationTime(DEC_31_2017_END), DEC_31_2017_END.minusDays(1));
    }

    @Test
    public void testGetEventNotificationTime() {
        assertEquals(getEventNotificationTime(JAN_1_2018_START), DEC_31_2017_END.withTime(23, 30,0,0));
        assertEquals(getEventNotificationTime(FEB_4_2018_END), FEB_4_2018_END.minusMinutes(30));
        assertEquals(getEventNotificationTime(DEC_31_2017_END), DEC_31_2017_END.minusMinutes(30));
    }

}
