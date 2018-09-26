package com.vish.travelbook.model;

import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

public class Trip implements Serializable {

    public int      id;
    public String   title;
    public DateTime startDate;
    public DateTime endDate;
    public String   image;

    public List<ItineraryEvent> events       = new ArrayList<>();
    public List<PackingItem>    packingItems = new ArrayList<>();
    public List<Expense>        expenses     = new ArrayList<>();

    public Trip() {
    }

    public Trip(String title, DateTime startDate, DateTime endDate, @Nullable String image) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.image = image;
    }

    public String getImage() {
        if (image == null) {
            return "image";
        } else {
            return image;
        }
    }
}
