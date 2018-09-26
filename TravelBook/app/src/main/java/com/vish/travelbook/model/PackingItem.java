package com.vish.travelbook.model;

import androidx.annotation.Nullable;
import java.io.Serializable;

public class PackingItem implements Serializable {

    public String item;
    public int    quantity;

    public PackingItem() {
    }

    public PackingItem(String item, int quantity) {
        this.item = item;
        this.quantity = quantity;
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
