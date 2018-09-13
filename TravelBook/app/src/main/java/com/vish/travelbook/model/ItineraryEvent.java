package com.vish.travelbook.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

public class ItineraryEvent implements Serializable {

    public String   title;
    public String   description;
    public DateTime startTime;
    public DateTime endTime;

    public ItineraryEvent(String title, String description, DateTime startTime, DateTime endTime) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static List<ItineraryEvent> mock(int num) {
        ArrayList<ItineraryEvent> events = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            events.add(new ItineraryEvent("title " + i, "desc " + i, DateTime.now().plusHours(i), DateTime.now().plusHours(i + 1)));
        }
        return events;
    }
}
