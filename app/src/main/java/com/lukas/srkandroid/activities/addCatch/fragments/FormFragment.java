package com.lukas.srkandroid.activities.addCatch.fragments;

import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.lukas.srkandroid.entities.Coords;
import com.lukas.srkandroid.entities.Fish;
import com.lukas.srkandroid.entities.User;
import com.lukas.srkandroid.entities.interfaces.SelectBoxItem;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormFragment extends Fragment {

    private final int RIGHT_HEAD_PHOTO_ID = 2;
    private final int LEFT_HEAD_PHOTO_ID = 3;
    private final int OPTIONAL_PHOTO_ID = 4;

    private AddCatchController controller;

    private List<User> users;
    private List<Fish> fishes;
    private List<Condition> conditions;
    private LatLng location;
    Calendar c = Calendar.getInstance();

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
    private View formLayout;
    private ProgressBar progress;

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
        c.setTimeInMillis(System.currentTimeMillis());

        controller.fetchUsers();
        controller.fetchFishes();
        controller.fetchConditions();

        dateText = view.findViewById(R.id.dateText);
        dateText.setText(calendarToString(c));
        addCatchBtn = view.findViewById(R.id.addCatchBtn);
        addCatchBtn.setVisibility(View.VISIBLE);
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
        formLayout = view.findViewById(R.id.formLayout);
        progress = view.findViewById(R.id.progress);
        progress.setVisibility(View.INVISIBLE);

        handleUserSelect();

        dateText.setOnClickListener(e -> pickDate());
        locationText.setOnClickListener(e -> pickLocation());
        rightHeadPhoto.setOnClickListener(e -> pickPhoto(RIGHT_HEAD_PHOTO_ID));
        leftHeadPhoto.setOnClickListener(e -> pickPhoto(LEFT_HEAD_PHOTO_ID));
        optionalPhotoInput.setOnClickListener(e -> pickPhoto(OPTIONAL_PHOTO_ID));
        addCatchBtn.setOnClickListener(e -> { addCatch(); });
        return view;
    }

    private void handleUserSelect() {
        if (!((AddCatch) getActivity()).getUser().isAdmin()) {
            userSelect.setVisibility(View.GONE);
        }
    }

    public void clear() {
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
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

    public void setLoading(boolean loading) {
        if (loading) {
            addCatchBtn.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
            return;
        }
        addCatchBtn.setVisibility(View.VISIBLE);
        progress.setVisibility(View.INVISIBLE);
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
        setLoading(true);
        String errorMessage = validateForm();
        if (errorMessage != null) {
            setLoading(false);
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            return;
        }
        Catch catch0 = createCatchInstanceFromForm();
        setLoading(true);
        controller.addCatch(catch0, rightHeadPhotoBmp, leftHeadPhotoBmp, optionalPhotos);
    }

    private String validateForm() {
        if (((AddCatch)getActivity()).getUser().isAdmin() && userSelect.getSelectedItem() == null) {
            return "Treba vybrať lovca odchytu.";
        }
        if (fishSelect.getSelectedItem() == null) {
            return "Treba vybrať rybu.";
        }
        if (locationText.getText().toString().isEmpty()) {
            return "Treba zadať polohu odchytu.";
        }
        if (weightText.getText().toString().isEmpty()) {
            return "Treba zadať váhu ryby.";
        }
        if (lengthText.getText().toString().isEmpty()) {
            return "Treba zadať dĺžku ryby.";
        }
        if (conditionSelect.getSelectedItem() == null) {
            return "Treba vybrať celkový stav ryby.";
        }
        if (conditionDescriptionText.getText().toString().isEmpty()) {
            return "Treba zadať celkový stav a kondíciu ryby (popis).";
        }
        if (rightHeadPhotoBmp == null) {
            return "Treba vybrať fotku hlavy ryby sprava.";
        }
        if (leftHeadPhotoBmp == null) {
            return "Treba vybrať fotku hlavy ryby zľava.";
        }
        return null;
    }

    private Catch createCatchInstanceFromForm() {
        Catch catch0 = new Catch();
        catch0.setAdded(getDateTimeInFormat());
        User loggedUser = ((AddCatch)getActivity()).getUser();
        if (loggedUser.isAdmin()) {
            catch0.setFisher((User) userSelect.getSelectedItem());
        }
        else {
            catch0.setFisher(loggedUser);
        }
        catch0.setFish((Fish) fishSelect.getSelectedItem());
        catch0.setCoords(locationToCoords());
        catch0.setWeight(Double.parseDouble(weightText.getText().toString().trim()));
        catch0.setLength(Double.parseDouble(lengthText.getText().toString().trim()));
        catch0.setCondition((Condition) conditionSelect.getSelectedItem());
        catch0.setHealthCondition(conditionDescriptionText.getText().toString().trim());
        String height = heightText.getText().toString().trim();
        String circuit = circuitText.getText().toString().trim();
        catch0.setHeight(height.isEmpty() ? null : Double.valueOf(height));
        catch0.setCircuit(circuit.isEmpty() ? null : Double.valueOf(circuit));
        String trap = trapText.getText().toString().trim();
        String notes = notesText.getText().toString().trim();
        catch0.setTrap(trap.isEmpty() ? null : trap);
        catch0.setNotes(notes.isEmpty() ? null : notes);
        return catch0;
    }

    @SuppressLint("DefaultLocale")
    private String getDateTimeInFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%04d", c.get(Calendar.YEAR)));
        sb.append("-");
        sb.append(String.format("%02d", c.get(Calendar.MONTH) + 1));
        sb.append("-");
        sb.append(String.format("%02d", c.get(Calendar.DAY_OF_MONTH)));
        sb.append("T");
        sb.append(String.format("%02d", c.get(Calendar.HOUR_OF_DAY)));
        sb.append(":");
        sb.append(String.format("%02d", c.get(Calendar.MINUTE)));
        sb.append(":00.000Z");
        return sb.toString();
    }

    private Coords locationToCoords() {
        Coords coords = new Coords();
        coords.setLat(location.latitude);
        coords.setLng(location.longitude);
        return coords;
    }

}
