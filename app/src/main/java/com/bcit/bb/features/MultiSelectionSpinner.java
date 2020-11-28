package com.bcit.bb.features;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Feature to have spinner with multiselection capabilities.
 * @Source  https://trinitytuts.com/tips/multiselect-spinner-item-in-android/
 */
public class MultiSelectionSpinner extends androidx.appcompat.widget.AppCompatSpinner implements
        OnMultiChoiceClickListener {
    String[] _items = null;
    boolean[] mSelection = null;

    ArrayAdapter<String> simple_adapter;

    /**
     * Constructor
     * @param context c
     */
    public MultiSelectionSpinner(Context context) {
        super(context);

        simple_adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(simple_adapter);
    }

    /**
     * Constructor.
     * @param context v
     * @param attrs a
     */
    public MultiSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        simple_adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(simple_adapter);
    }

    /**
     * onClick handler.
     * @param dialog d
     * @param which index
     * @param isChecked checked boolean
     */
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

        if (mSelection != null && which < mSelection.length) {
            mSelection[which] = isChecked;

            simple_adapter.clear();
            simple_adapter.add(buildSelectedItemString());

        } else {
            throw new IllegalArgumentException(
                    "Argument 'which' is out of bounds.");
        }

    }

    /**
     * Checks for checked.
     * @return
     */
    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(_items, mSelection, this);
        builder.show();
        return true;
    }

    /**
     * Set adapter.
     * @param adapter adapter
     */
    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException(
                "setAdapter is not supported by MultiSelectSpinner.");
    }

    /**
     * Set items.
     * @param items items
     */
    public void setItems(String[] items) {
        _items = items;
        mSelection = new boolean[_items.length];
        simple_adapter.clear();
        simple_adapter.add(_items[0]);
        Arrays.fill(mSelection, false);
    }

    public void setItems(List<String> items) {
        _items = items.toArray(new String[items.size()]);
        mSelection = new boolean[_items.length];


            simple_adapter.clear();
            simple_adapter.add(_items[0]);
            Arrays.fill(mSelection, false);

    }




    public void setSelection(int index) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        if (index >= 0 && index < mSelection.length) {
            mSelection[index] = true;
        } else {
            throw new IllegalArgumentException("Index " + index
                    + " is out of bounds.");
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }


    /**
     * Build string.
     * @return string
     */
    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < _items.length ; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;
                sb.append(_items[i]);
            }
        }
        return sb.toString();
    }

    /**
     * Get selected items as string.
     * @return string
     */
    public String getSelectedItemsAsString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;
                sb.append(_items[i]);
            }
        }
        return sb.toString();
    }

    /**
     * Get selections.
     * @return selection.
     */
    public int getSelections(){
        int selected = 0;
        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                selected++;
            }
        }
        return selected;

    }
}