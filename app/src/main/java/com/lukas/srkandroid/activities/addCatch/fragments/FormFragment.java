package com.lukas.srkandroid.activities.addCatch.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;
import com.lukas.srkandroid.R;
import com.lukas.srkandroid.activities.addCatch.AddCatchController;
import com.lukas.srkandroid.activities.addCatch.NothingSelectedSelectBoxAdapter;
import com.lukas.srkandroid.activities.addCatch.SelectBoxItemAdapter;
import com.lukas.srkandroid.activities.addCatch.AddCatch;
import com.lukas.srkandroid.activities.addCatch.ThreeDecimalPlacesNumberFilter;
import com.lukas.srkandroid.entities.Condition;
import com.lukas.srkandroid.entities.Fish;
import com.lukas.srkandroid.entities.User;
import com.lukas.srkandroid.entities.interfaces.SelectBoxItem;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class FormFragment extends Fragment {

    private AddCatchController controller;

    private List<User> users;
    private List<Fish> fishes;
    private List<Condition> conditions;
    private LatLng location;
    Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC+01:00"));

    private Button addCatchBtn;
    private EditText dateText;
    private Spinner userSelect;
    private Spinner fishSelect;
    private Spinner conditionSelect;
    private EditText locationText;
    private EditText weightText;
    private EditText lengthText;
    private EditText heightText;
    private EditText circuitText;
    private EditText trapText;
    private EditText conditionDescriptionText;
    private EditText notesText;

    public FormFragment() {
        controller = new AddCatchController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form, container, false);

        controller.fetchUsers();
        controller.fetchFishes();
        controller.fetchConditions();

        dateText = view.findViewById(R.id.dateText);
        dateText.setText(calendarToString(c));
        addCatchBtn = view.findViewById(R.id.addCatchBtn);
        userSelect = view.findViewById(R.id.userSelect);
        fishSelect = view.findViewById(R.id.fishSelect);
        conditionSelect = view.findViewById(R.id.conditionSelect);
        locationText = view.findViewById(R.id.locationText);
        weightText = view.findViewById(R.id.weightText);
        lengthText = view.findViewById(R.id.lengthText);
        heightText = view.findViewById(R.id.heightText);
        circuitText = view.findViewById(R.id.circuitText);
        weightText.setFilters(new InputFilter[]{new ThreeDecimalPlacesNumberFilter()});
        lengthText.setFilters(new InputFilter[]{new ThreeDecimalPlacesNumberFilter()});
        heightText.setFilters(new InputFilter[]{new ThreeDecimalPlacesNumberFilter()});
        circuitText.setFilters(new InputFilter[]{new ThreeDecimalPlacesNumberFilter()});
        trapText = view.findViewById(R.id.trapText);
        conditionDescriptionText = view.findViewById(R.id.conditionDescriptionText);
        notesText = view.findViewById(R.id.notesText);

        dateText.setOnClickListener(e -> pickDate());
        locationText.setOnClickListener(e -> pickLocation());
        addCatchBtn.setOnClickListener(e -> {
            // TODO pridaj odchyt
        });
        return view;
    }

    public void handleFetchDataError() {
        // TODO
    }

    public void setUsers(List<User> users) {
        this.users = users;
        NothingSelectedSelectBoxAdapter adapter = createSelectBoxAdapter(
                R.layout.spinner_item_nothing_selected_fisher,
                users.toArray(new User[]{})
        );
        userSelect.setAdapter(adapter);
    }

    public void setFishes(List<Fish> fishes) {
        this.fishes = fishes;
        NothingSelectedSelectBoxAdapter adapter = createSelectBoxAdapter(
                R.layout.spinner_item_nothing_selected_fish,
                fishes.toArray(new Fish[]{})
        );
        fishSelect.setAdapter(adapter);
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
        NothingSelectedSelectBoxAdapter adapter = createSelectBoxAdapter(
                R.layout.spinner_item_nothing_selected_condition,
                conditions.toArray(new Condition[]{})
        );
        conditionSelect.setAdapter(adapter);
    }

    public void setLocation(LatLng location) {
        this.location = location;
        locationText.setText(String.format("lat=%f, lng=%f", location.latitude, location.longitude));
    }

    private void pickLocation() {
        ((AddCatch)getActivity()).showMap();
    }

    private void pickDate(){
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, year0, month0, dayOfMonth0) -> {
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (view1, hourOfDay0, minute0) -> {
                c.set(year0, month0, dayOfMonth0, hourOfDay0, minute0);
                dateText.setText(calendarToString(c));
            }, hour, minute, DateFormat.is24HourFormat(getActivity()));
            timePickerDialog.show();
        }, year, month, day);
        datePickerDialog.show();
    }

    private static String calendarToString(Calendar c){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02d.", c.get(Calendar.DAY_OF_MONTH)));
        sb.append(String.format("%02d.", c.get(Calendar.MONTH)+1));
        sb.append(String.format("%04d ", c.get(Calendar.YEAR)));
        sb.append(String.format("%02d:", c.get(Calendar.HOUR_OF_DAY)));
        sb.append(String.format("%02d", c.get(Calendar.MINUTE)));
        return sb.toString();
    }

    private NothingSelectedSelectBoxAdapter createSelectBoxAdapter(int nothingSelectedLayout, SelectBoxItem[] items) {
        return new NothingSelectedSelectBoxAdapter(
                new SelectBoxItemAdapter(
                        getActivity(), R.layout.spinner_item, items
                ),
                nothingSelectedLayout,
                getActivity()
        );
    }
}
