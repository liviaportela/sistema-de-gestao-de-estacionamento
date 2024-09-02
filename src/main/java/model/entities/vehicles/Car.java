package model.entities.vehicles;

import model.entities.*;
import model.enums.Categories;
import model.enums.TypeVehicle;

public class Car extends Vehicle {

    private TypeVehicle typeVehicle;

    public Car() {
    }

    public Car(Categories categories, TypeVehicle typeVehicle) {
        this.setCategory(categories);
        this.setTypeVehicle(typeVehicle);
    }

    public Car(Integer id, String plate, Categories category, TypeVehicle typeVehicle) {
        this.setId(id);
        this.setPlate(plate);
        this.setCategory(category);
        this.typeVehicle = typeVehicle;
    }

    public Car(String plate, Categories category, TypeVehicle typeVehicle) {
        this.setPlate(plate);
        this.setCategory(category);
        this.typeVehicle = typeVehicle;
    }

    public Car(Integer id, Gate gate, Vacancy vacancy, String plate, Categories category, Double payment, TypeVehicle typeVehicle) {
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

    @Override
    public Integer getRequiredVacancies() {
        return 2;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + getId() +
                ", plate='" + getPlate() + '\'' +
                ", typeVehicle=" + getTypeVehicle() +
                ", category=" + getCategory() +
                '}';
    }
}
