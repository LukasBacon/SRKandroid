package com.lukas.srkandroid.activities.addCatch.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.lukas.srkandroid.R;
import com.lukas.srkandroid.activities.addCatch.AddCatchController;
import com.lukas.srkandroid.activities.addCatch.fragments.formInputsClasses.NothingSelectedSelectBoxAdapter;
import com.lukas.srkandroid.activities.addCatch.fragments.formInputsClasses.SelectBoxItemAdapter;
import com.lukas.srkandroid.activities.addCatch.AddCatch;
import com.lukas.srkandroid.activities.addCatch.fragments.formInputsClasses.ThreeDecimalPlacesNumberFilter;
import com.lukas.srkandroid.entities.Catch;
import com.lukas.srkandroid.entities.Condition;
import com.lukas.srkandroid.entities.Fish;
import com.lukas.srkandroid.entities.User;
import com.lukas.srkandroid.entities.interfaces.SelectBoxItem;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class FormFragment extends Fragment {

    private final int RIGHT_HEAD_PHOTO_ID = 2;
    private final int LEFT_HEAD_PHOTO_ID = 3;
    private final int OPTIONAL_PHOTO_ID = 4;

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

    private ImageView rightHeadPhoto;
    private Bitmap rightHeadPhotoBmp;

    private ImageView leftHeadPhoto;
    private Bitmap leftHeadPhotoBmp;

    private ImageView optionalPhotoInput;
    private LinearLayout optionalPhotosList;
    private Map<ImageView,Bitmap> optionalPhotos = new HashMap<>();

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
        rightHeadPhoto = view.findViewById(R.id.rightHeadPhoto);
        leftHeadPhoto = view.findViewById(R.id.leftHeadPhoto);
        optionalPhotoInput = view.findViewById(R.id.optionalPhotoInput);
        optionalPhotosList = view.findViewById(R.id.optionalPhotosList);

        dateText.setOnClickListener(e -> pickDate());
        locationText.setOnClickListener(e -> pickLocation());
        rightHeadPhoto.setOnClickListener(e -> pickPhoto(RIGHT_HEAD_PHOTO_ID));
        leftHeadPhoto.setOnClickListener(e -> pickPhoto(LEFT_HEAD_PHOTO_ID));
        optionalPhotoInput.setOnClickListener(e -> pickPhoto(OPTIONAL_PHOTO_ID));
        addCatchBtn.setOnClickListener(e -> { addCatch(); });
        return view;
    }

    public void handleFetchDataError() {
        Toast.makeText(getContext(), "Chyba pri načítavaní údajov.", Toast.LENGTH_LONG).show();
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

    private void pickPhoto(int photoTypeId) {
        startActivityForResult(
                new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                photoTypeId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                if (requestCode == RIGHT_HEAD_PHOTO_ID) {
                    rightHeadPhotoBmp = bmp;
                    rightHeadPhoto.setImageBitmap(rightHeadPhotoBmp);
                }
                if (requestCode == LEFT_HEAD_PHOTO_ID) {
                    leftHeadPhotoBmp = bmp;
                    leftHeadPhoto.setImageBitmap(leftHeadPhotoBmp);
                }
                if (requestCode == OPTIONAL_PHOTO_ID) {
                    ImageView imageView = new ImageView(getContext());
                    optionalPhotos.put(imageView, bmp);
                    optionalPhotosList.addView(imageView);
                    imageView.setImageBitmap(bmp);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                    lp.width = 300;
                    lp.height = 300;
                    lp.setMargins(0, 20, 0, 0);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setOnClickListener(view -> {
                        optionalPhotos.remove(view);
                        ((ViewManager)view.getParent()).removeView(view);
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addCatch() {
        String errorMessage = validateForm();
        if (errorMessage != null) {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            return;
        }
        Catch catch0 = new Catch();
        // TODO napln a postni
    }

    private String validateForm() {
        // TODO validate
        return null;
    }

}
