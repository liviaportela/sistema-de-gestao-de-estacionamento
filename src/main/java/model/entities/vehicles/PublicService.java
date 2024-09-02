package model.entities.vehicles;

import model.entities.Gate;
import model.entities.Vacancy;
import model.entities.Vehicle;
import model.enums.Categories;
import model.enums.TypeVehicle;

public class PublicService extends Vehicle {

    public PublicService(Categories category){
        this.setCategory(category);
    }

    public PublicService(Integer id, Categories category){
        this.setId(id);
        this.setCategory(category);
    }

    public PublicService(Integer id, Gate gate, Vacancy vacancy, String plate, Categories category, Double payment) {
        super(id, gate, vacancy, plate, category, payment);
    }

    @Override
    public Integer getRequiredVacancies() {
        return 0;
    }

    @Override
    public TypeVehicle getTypeVehicle() {
        return TypeVehicle.SERVICO_PUBLICO;
    }
}
