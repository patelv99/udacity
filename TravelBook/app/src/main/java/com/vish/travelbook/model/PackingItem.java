package com.vish.travelbook.model;

import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PackingItem implements Serializable {

    public String item;
    public int    quantity;

    public PackingItem() {

    }

    public PackingItem(String item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public static List<PackingItem> mock(int num) {
        ArrayList<PackingItem> packingItems = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            packingItems.add(new PackingItem("desc " + i, i + 1));
        }
        return packingItems;
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj instanceof PackingItem) {
            return ((PackingItem) obj).item.equals(item) && ((PackingItem) obj).quantity == quantity;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return quantity * item.hashCode();
    }
}
