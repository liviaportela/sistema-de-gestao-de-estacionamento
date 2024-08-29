package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.VehicleDao;
import model.entities.Vehicle;
import model.enums.Categories;

import java.sql.*;
import java.util.List;

import static model.enums.Categories.MENSALISTA;

public class VehicleDaoJDBC implements VehicleDao {

    private Connection conn;

    public VehicleDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Vehicle obj) {

        int categoryId = getCategoryID(obj.getCategory());

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO vehicle" +
                            "(Plate, IdCategory) " +
                            "VALUES " +
                            "(?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getPlate());
            st.setInt(2, categoryId);

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    public int getCategoryID(Categories category) {
        switch (category) {
            case MENSALISTA:
                return 1;
            case CAMINHAO:
                return 2;
            case SERVICO_PUBLICO:
                return 3;
            case AVULSO:
                return 4;
            default:
                throw new IllegalArgumentException("Categoria desconhecida: " + category);
        }
    }

    @Override
    public void update(Vehicle obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Vehicle findById(Integer id) {
        return null;
    }

    @Override
    public List<Vehicle> findAll() {
        return List.of();
    }
}
