package model.dao;

import model.entities.Management;
import model.entities.Vacancy;

import java.util.List;

public interface ManagementDao {

    void insert(Management obj);
    void update(Management obj);
    void deleteById(Integer id);
    Management findById(Integer id);
    List<Management> findAll();
}
