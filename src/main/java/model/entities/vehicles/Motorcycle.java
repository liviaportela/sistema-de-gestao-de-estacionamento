package model.entities.vehicles;

import model.entities.*;
import model.enums.Categories;
import model.enums.TypeVehicle;

public class Motorcycle extends Vehicle {

    private TypeVehicle typeVehicle;

    public Motorcycle() {
    }

    public Motorcycle(Integer id, String plate, Categories category, TypeVehicle typeVehicle) {
        this.setId(id);
        this.setPlate(plate);
        this.setCategory(category);
        this.typeVehicle = typeVehicle;
    }

    public Motorcycle(Categories categories, TypeVehicle typeVehicle) {
        this.setCategory(categories);
        this.setTypeVehicle(typeVehicle);
    }

    public Motorcycle(String plate, Categories category, TypeVehicle typeVehicle) {
        this.setPlate(plate);
        this.setCategory(category);
        this.typeVehicle = typeVehicle;
    }

    public Motorcycle(Integer id, Gate gate, Vacancy vacancy, String plate, Categories category, Double payment, TypeVehicle typeVehicle) {
        super(id, gate, vacancy, plate, category, payment);
        this.typeVehicle = typeVehicle;
    }

    @Override
    public TypeVehicle getTypeVehicle() {
        return typeVehicle;
    }

    public void setTypeVehicle(TypeVehicle typeVehicle) {
        this.typeVehicle = typeVehicle;
    }

    public Double payment() {
        Double payment;
        if (getCategory().equals(Categories.MENSALISTA)) {
            return payment = 250.0;
        } else {
            return payment = null;
        }
    }

    @Override
    public Integer getRequiredVacancies() {
        return 1;
    }

    @Override
    public String toString() {
        return "Motorcycle{" +
                "id=" + getId() +
                ", plate='" + getPlate() + '\'' +
                ", typeVehicle=" + getTypeVehicle() +
                ", category=" + getCategory() +
                '}';
    }
}
