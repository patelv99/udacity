package com.vish.travelbook;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    public static final String PACKING_ITEM_KEY = "packing_item_key";

    private TextView title;
    private EditText quantityEditText;
    private EditText itemEditText;
    private Button   saveButton;

    private int         quantity;
    private String      item;
    private boolean     modifying   = false;
    private PackingItem packingItem = new PackingItem();

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
            if (getArguments().getSerializable(PACKING_ITEM_KEY) != null) {
                packingItem = (PackingItem) getArguments().getSerializable(PACKING_ITEM_KEY);
                modifyPackingItem();
                setHasOptionsMenu(true);
            }
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (modifying) {
            inflater.inflate(R.menu.menu_trip, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showProgressDialog();
                deletePackingItem(title, packingItem);
                dismissProgressDialog();
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Modify the selected packing item
     */
    private void modifyPackingItem() {
        modifying = true;
        itemEditText.setText(packingItem.item);
        quantityEditText.setText(Integer.toString(packingItem.quantity));
    }

    /**
     * Save the packing item and trip on save click
     */
    private void onSaveClick() {
        if (validateItem()) {
            showProgressDialog();
            if (modifying) {
                int index = trip.packingItems.indexOf(packingItem);
                packingItem.item = item;
                packingItem.quantity = quantity;
                trip.packingItems.set(index, packingItem);
            } else {
                packingItem.item = item;
                packingItem.quantity = quantity;
                trip.packingItems.add(packingItem);
            }
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
