package model.dao;

import model.entities.Vacancy;
import model.entities.Vehicle;

import java.util.List;

public interface VacancyDao {

    void insert(Vacancy obj);
    void update(Vacancy obj);
    List<Vacancy> findAll();
    List<Vacancy> findByVehicleId(Integer vehicleId);
    boolean allocateVacancies(Integer vehicleId, int requiredVacancies);
    boolean releaseVacancies(Integer vehicleId);
}
