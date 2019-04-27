package com.lukas.srkandroid.entities;

import com.lukas.srkandroid.entities.interfaces.WritableToJSON;

import org.json.JSONException;
import org.json.JSONObject;

public class Catch implements WritableToJSON {

    private Integer id;
    private User fisher;
    private Fish fish;
    private Coords coords;
    private Double weight;
    private Double length;
    private Double height;
    private Double circuit;
    private Condition condition;
    private String healthCondition;
    private String trap;
    private String notes;
    private String added;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getFisher() {
        return fisher;
    }

    public void setFisher(User fisher) {
        this.fisher = fisher;
    }

    public Fish getFish() {
        return fish;
    }

    public void setFish(Fish fish) {
        this.fish = fish;
    }

    public Coords getCoords() {
        return coords;
    }

    public void setCoords(Coords coords) {
        this.coords = coords;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getCircuit() {
        return circuit;
    }

    public void setCircuit(Double circuit) {
        this.circuit = circuit;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String getHealthCondition() {
        return healthCondition;
    }

    public void setHealthCondition(String healthCondition) {
        this.healthCondition = healthCondition;
    }

    public String getTrap() {
        return trap;
    }

    public void setTrap(String trap) {
        this.trap = trap;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    @Override
    public String toString() {
        return "Catch{" +
                "id=" + id +
                ", fisher=" + fisher +
                ", fish=" + fish +
                ", coords=" + coords +
                ", weight=" + weight +
                ", length=" + length +
                ", height=" + height +
                ", circuit=" + circuit +
                ", condition=" + condition +
                ", healthCondition='" + healthCondition + '\'' +
                ", trap='" + trap + '\'' +
                ", notes='" + notes + '\'' +
                ", added='" + added + '\'' +
                '}';
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("fisher", fisher.toJSON());
        jsonObject.put("fish", fish.toJSON());
        jsonObject.put("coords", coords.toJSON());
        jsonObject.put("weight", weight);
        jsonObject.put("length", length);
        jsonObject.put("height", height);
        jsonObject.put("circuit", circuit);
        jsonObject.put("condition", condition.toJSON());
        jsonObject.put("healthCondition", healthCondition);
        jsonObject.put("trap", trap);
        jsonObject.put("notes", notes);
        jsonObject.put("added", added);
        return jsonObject;
    }
}
