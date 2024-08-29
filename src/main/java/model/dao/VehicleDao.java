package model.dao;

import model.entities.Vehicle;

import java.util.List;

public interface VehicleDao {

    void insert(Vehicle obj);
    void update(Vehicle obj);
    void deleteById(Integer id);
    Vehicle findById(Integer id);
    List<Vehicle> findAll();
}
