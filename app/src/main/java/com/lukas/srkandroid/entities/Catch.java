package com.lukas.srkandroid.entities;

public class Catch {

    private Integer id;
    private User fisher;
    private Fish fish;
    private Coords coords;
    private double weight;
    private double length;
    private double height;
    private double circuit;
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

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getCircuit() {
        return circuit;
    }

    public void setCircuit(double circuit) {
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

}
