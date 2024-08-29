package model.entities;

import model.enums.Categories;

import java.io.Serializable;

public abstract class Vehicle implements Serializable {

    private Integer id;
    private Gate gate;
    private Vacancy vacancy;
    private String plate;
    private Categories category;

    public Vehicle() {
    }

    public Vehicle(Integer id, Gate gate, Vacancy vacancy, String plate, Categories category) {
        this.id = id;
        this.gate = gate;
        this.vacancy = vacancy;
        this.plate = plate;
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Gate getGate() {
        return gate;
    }

    public void setGate(Gate gate) {
        this.gate = gate;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }
}
