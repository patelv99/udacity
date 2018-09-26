package com.vish.travelbook.model;

import androidx.annotation.Nullable;
import java.io.Serializable;
import org.joda.time.DateTime;

public class ItineraryEvent implements Serializable {

    public String   title;
    public String   description;
    public DateTime startTime;
    public DateTime endTime;

    public ItineraryEvent() {
    }

    public ItineraryEvent(String title, String description, DateTime startTime, DateTime endTime) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj instanceof ItineraryEvent) {
            return ((ItineraryEvent) obj).title.equals(title)
                   && ((ItineraryEvent) obj).startTime.isEqual(startTime)
                   && ((ItineraryEvent) obj).endTime.isEqual(endTime);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return title.hashCode() * startTime.hashCode() * endTime.hashCode();
    }
}
