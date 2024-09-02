package model.entities.vehicles;

import model.entities.Gate;
import model.entities.Vacancy;
import model.entities.Vehicle;
import model.enums.Categories;
import model.enums.TypeVehicle;

public class Truck extends Vehicle {

    public Truck() {
    }

    public Truck(Integer id, String plate, Categories category) {
        this.setId(id);
        this.setPlate(plate);
        this.setCategory(category);
    }

    public Truck(String plate, Categories category) {
        this.setPlate(plate);
        this.setCategory(category);
    }

    public Truck(Integer id, Gate gate, Vacancy vacancy, String plate, Categories category, Double payment) {
        super(id, gate, vacancy, plate, category, payment);
    }

    @Override
    public Integer getRequiredVacancies() {
        return 4;
    }

    @Override
    public TypeVehicle getTypeVehicle() {
        return TypeVehicle.CAMINHAO;
    }
}
