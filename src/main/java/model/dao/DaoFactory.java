package model.dao;

import db.DB;
import model.dao.impl.ManagementDaoJDBC;
import model.dao.impl.VacancyDaoJDBC;
import model.dao.impl.VehicleDaoJDBC;

public class DaoFactory {

    public static VehicleDao createVehicleDao(){
        return new VehicleDaoJDBC(DB.getConnection());
    }
    public static ManagementDao createManagementDao(){
        return new ManagementDaoJDBC(DB.getConnection());
    }
    public static VacancyDao createVacancyDao(){
        return new VacancyDaoJDBC(DB.getConnection());
    }
}
