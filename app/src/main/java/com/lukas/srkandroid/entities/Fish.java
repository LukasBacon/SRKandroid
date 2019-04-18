package com.lukas.srkandroid.entities;

import com.lukas.srkandroid.entities.interfaces.LoadableFromJSON;
import com.lukas.srkandroid.entities.interfaces.SelectBoxItem;

import org.json.JSONException;
import org.json.JSONObject;

public class Fish implements SelectBoxItem, LoadableFromJSON {

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
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append(", ");
        if (!name.isEmpty()) {
            sb.append(name);
            sb.append(", ");
        }
        sb.append(species.getNameSk());
        sb.append(" (");
        sb.append(species.getNameLat());
        sb.append(")");
        return sb.toString();
    }

    @Override
    public Fish loadFromJsonObject(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getString("id");
        name = jsonObject.getString("name");
        species = new Species().loadFromJsonObject(jsonObject.getJSONObject("species"));
        return this;
    }
}
