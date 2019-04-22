package com.lukas.srkandroid.activities.addCatch.fragments.formInputsClasses;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lukas.srkandroid.entities.interfaces.SelectBoxItem;

public class SelectBoxItemAdapter extends ArrayAdapter<SelectBoxItem> {

    private SelectBoxItem[] values;

    public SelectBoxItemAdapter(Context context, int itemView, SelectBoxItem[] values) {
        super(context, itemView, values);
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.length;
    }

    @Override
    public SelectBoxItem getItem(int position){
        return values[position];
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        return getLabel(label, position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        return getLabel(label, position);
    }

    private TextView getLabel(TextView label, int position) {
        label.setTextColor(Color.BLACK);
        label.setText(getItem(position).toSelectBoxLabel());
        return label;
    }
}
