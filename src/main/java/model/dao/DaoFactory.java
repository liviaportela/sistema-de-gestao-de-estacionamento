package model.dao;

import db.DB;
import model.dao.impl.ManagementDaoJDBC;
import model.dao.impl.VehicleDaoJDBC;

public class DaoFactory {

    public static VehicleDao createVeiculoDao(){
        return new VehicleDaoJDBC(DB.getConnection());
    }

}
