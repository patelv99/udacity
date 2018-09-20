package com.vish.travelbook.model;

import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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

    public static List<Expense> mock(int num) {
        ArrayList<Expense> expenses = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            expenses.add(new Expense("desc " + i, num - i, DateTime.now().plusHours(i)));
        }
        return expenses;
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
