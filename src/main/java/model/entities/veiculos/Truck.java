package model.entities.veiculos;

import model.entities.Gate;
import model.entities.Vacancy;
import model.entities.Vehicle;
import model.enums.Categories;

public class Truck extends Vehicle {

    private Integer requiredVacancies = 4;

    public Truck() {
    }

    public Truck(String plate, Categories category) {
        this.setPlate(plate);
        this.setCategory(category);
    }

    public Truck(Integer id, Gate gate, Vacancy vacancy, String plate, Categories category, Integer requiredVacancies) {
        super(id, gate, vacancy, plate, category);
        this.requiredVacancies = requiredVacancies;
    }

    public Integer getRequiredVacancies() {
        return requiredVacancies;
    }

    public void setRequiredVacancies(Integer requiredVacancies) {
        this.requiredVacancies = requiredVacancies;
    }
}
