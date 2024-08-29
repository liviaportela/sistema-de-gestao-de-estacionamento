package model.entities.veiculos;

import model.entities.*;
import model.entities.interfaces.Monthly;
import model.enums.Categories;

public class Car extends Vehicle implements Monthly {

    private Integer requiredVacancies = 2;

    public Car() {
    }

    public Car(String plate, Categories category) {
        this.setPlate(plate);
        this.setCategory(category);
    }

    public Car(Integer id, Gate gate, Vacancy vacancy, String plate, Categories category, Integer requiredVacancies) {
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
