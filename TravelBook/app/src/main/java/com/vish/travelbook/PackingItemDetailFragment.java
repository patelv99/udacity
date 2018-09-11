package com.vish.travelbook;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import com.vish.travelbook.model.PackingItem;
import com.vish.travelbook.model.Trip;

import static com.vish.travelbook.TripDetailActivity.TRIP_KEY;

public class PackingItemDetailFragment extends BaseFragment {

    private TextView title;
    private EditText quantityEditText;
    private EditText itemEditText;
    private Button   saveButton;

    private int    quantity;
    private String item;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_packing_item, container, false);
        title = view.findViewById(R.id.add_packing_item_title);
        quantityEditText = view.findViewById(R.id.add_packing_item_quantity);
        itemEditText = view.findViewById(R.id.add_packing_item_item);
        saveButton = view.findViewById(R.id.add_packing_item_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                onSaveClick();
            }
        });

        if (getArguments() != null && getArguments().getSerializable(TRIP_KEY) != null) {
            trip = (Trip) getArguments().getSerializable(TRIP_KEY);
        }

        return view;
    }

    private void onSaveClick() {
        if (validateItem()) {
            PackingItem packingItem = new PackingItem(item, quantity);
            trip.packingItems.add(packingItem);
            showProgressDialog();
            updateTripInDB(title);
            dismissProgressDialog();
            getActivity().finish();
        } else {
            Snackbar.make(title, R.string.invalid_packing_item, Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Validate that the item has a quantity greater than 0 and item is not null or empty
     *
     * @return boolean
     */
    private boolean validateItem() {
        quantity = Integer.parseInt(quantityEditText.getText().toString());
        item = itemEditText.getText().toString();
        return quantity > 0 && !TextUtils.isEmpty(item);
    }
}
