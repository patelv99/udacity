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

    public static List<Trip> mockTrips(int num) {
        List<Trip> trips = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            trips.add(mock(i));
        }
        return trips;
    }

    public static Trip mock(int num) {
        Trip trip = new Trip("Trip " + num, DateTime.now().plusDays(num), DateTime.now().plusDays(num * 2), "image");
        trip.events = new ArrayList<>(ItineraryEvent.mock(num));
        trip.packingItems = new ArrayList<>(PackingItem.mock(num));
        trip.expenses = new ArrayList<>(Expense.mock(num));
        return trip;
    }
}
