package com.vish.travelbook.model;

import androidx.annotation.Nullable;
import java.io.Serializable;
import org.joda.time.DateTime;

public class Expense implements Serializable {

    public String   description;
    public double   amount;
    public DateTime dateTime;

    public Expense() {
    }

    public Expense(String description, double amount, DateTime dateTime) {
        this.description = description;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj instanceof Expense) {
            return ((Expense) obj).description.equals(description)
                   && ((Expense) obj).amount == amount
                   && ((Expense) obj).dateTime.isEqual(dateTime);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (int) (description.hashCode() * amount * dateTime.hashCode());
    }
}
