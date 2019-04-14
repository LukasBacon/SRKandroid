package com.lukas.srkandroid.entities;

import com.lukas.srkandroid.entities.interfaces.SelectBoxItem;

public class Fish implements SelectBoxItem {

    private String id;
    private String name;
    private Species species;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    @Override
    public String toString() {
        return "Fish{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", species=" + species +
                '}';
    }

    @Override
    public String toSelectBoxLabel() {
        return name;
    }
}
