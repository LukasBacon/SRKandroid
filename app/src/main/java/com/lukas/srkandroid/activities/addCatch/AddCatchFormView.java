package com.lukas.srkandroid.activities.addCatch;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.lukas.srkandroid.R;
import com.lukas.srkandroid.entities.Catch;
import com.lukas.srkandroid.entities.Condition;
import com.lukas.srkandroid.entities.Fish;
import com.lukas.srkandroid.entities.User;
import com.lukas.srkandroid.entities.interfaces.SelectBoxItem;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class AddCatchFormView {

    private AddCatchActivity activity;
    private AddCatchController controller;

    private Catch catch0;
    private List<User> users;
    private List<Fish> fishes;
    private List<Condition> conditions;
    Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    private Button addCatchBtn;
    private EditText dateText;
    private Spinner userSelect;
    private Spinner fishSelect;
    private Spinner conditionSelect;

    public AddCatchFormView(AddCatchActivity activity, Catch catch0) {
        this.activity = activity;
        controller = new AddCatchController(this);
        this.catch0 = catch0;

        controller.fetchUsers();
        controller.fetchFishes();
        controller.fetchConditions();

        dateText = activity.findViewById(R.id.dateText);
        dateText.setText(calendarToString(c));
        addCatchBtn = activity.findViewById(R.id.add_catch_btn);
        userSelect = activity.findViewById(R.id.userSelect);
        fishSelect = activity.findViewById(R.id.fishSelect);
        conditionSelect = activity.findViewById(R.id.conditionSelect);

        dateText.setOnClickListener(e -> pickDate());
    }

    public AddCatchActivity getActivity() { return activity; }

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

    private void pickDate(){
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, (view, year0, month0, dayOfMonth0) -> {
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(activity, (view1, hourOfDay0, minute0) -> {
                c.set(year0, month0, dayOfMonth0, hourOfDay0, minute0);
                dateText.setText(calendarToString(c));
            }, hour, minute, DateFormat.is24HourFormat(activity));
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
                        activity, R.layout.spinner_item, items
                ),
                nothingSelectedLayout,
                activity
        );
    }

}
