package com.lukas.srkandroid.entities;

import com.lukas.srkandroid.entities.interfaces.SelectBoxItem;

public class Condition implements SelectBoxItem {

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Condition{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public String toSelectBoxLabel() {
        return name;
    }
}
