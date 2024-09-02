package model.dao;

import model.entities.Vehicle;
import model.enums.Categories;
import model.enums.TypeVehicle;

import java.util.List;

public interface VehicleDao {
    void insert(Vehicle obj);
    int getTypeVehicleID(TypeVehicle typeVehicle);
    void update(Vehicle obj);
    void deleteById(Integer id);
    Vehicle findById(Integer id);
    Vehicle findByPlate(String plate);
    List<Vehicle> findAll();
    int getCategoryID(Categories categories);
}
