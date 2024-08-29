package model.entities.veiculos;

import model.entities.Gate;
import model.entities.Vacancy;
import model.entities.Vehicle;
import model.enums.Categories;

public class PublicService extends Vehicle {

    public PublicService(Categories category){
        this.setCategory(category);
    }

    public PublicService(Integer id, Gate gate, Vacancy vacancy, String placa, Categories category) {
        super(id, gate, vacancy, placa, category);
    }
}
