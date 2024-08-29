package model.entities.veiculos;

import model.entities.*;
import model.enums.Categories;

public class Motorcycle extends Vehicle {

    private Integer requiredVacancies = 1;

    public Motorcycle(String plate, Categories category) {
        this.setPlate(plate);
        this.setCategory(category);
    }

    public Motorcycle(Integer id, Gate gate, Vacancy vacancy, String plate, Categories category, Integer requiredVacancies) {
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
