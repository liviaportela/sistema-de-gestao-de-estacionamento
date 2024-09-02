package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.VehicleDao;
import model.entities.Vehicle;
import model.entities.vehicles.Car;
import model.entities.vehicles.Motorcycle;
import model.entities.vehicles.PublicService;
import model.entities.vehicles.Truck;
import model.enums.Categories;
import model.enums.TypeVehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDaoJDBC implements VehicleDao {

    private Connection conn;

    public VehicleDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Vehicle obj) {
        int categoryId = getCategoryID(obj.getCategory());
        int typeVehicleId = getTypeVehicleID(obj.getTypeVehicle());

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO vehicle" +
                            "(Plate, IdCategory, IdTypeVehicle) " +
                            "VALUES " +
                            "(?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getPlate());
            st.setInt(2, categoryId);
            st.setInt(3, typeVehicleId);

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

    @Override
    public int getCategoryID(Categories categories) {
        switch (categories) {
            case MENSALISTA:
                return 1;
            case CAMINHAO:
                return 2;
            case SERVICO_PUBLICO:
                return 3;
            case AVULSO:
                return 4;
            default:
                throw new IllegalArgumentException("Categoria desconhecida: " + categories);
        }
    }

    @Override
    public int getTypeVehicleID(TypeVehicle typeVehicle) {
        switch (typeVehicle) {
            case CARRO:
                return 1;
            case MOTO:
                return 2;
            case CAMINHAO:
                return 3;
            case SERVICO_PUBLICO:
                return 4;
            default:
                throw new IllegalArgumentException("Categoria desconhecida: " + typeVehicle);
        }
    }

    @Override
    public void update(Vehicle obj) {
        int categoryId = getCategoryID(obj.getCategory());

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE vehicle " +
                            "SET Plate = ?, IdCategory = ? " +
                            "WHERE Id = ?");
            st.setString(1, obj.getPlate());
            st.setInt(2, categoryId);
            st.setInt(3, obj.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM vehicle " +
                    "WHERE Id = ?;");
            st.setInt(1, id);
            int rows = st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    private Vehicle instantiateVehicle(ResultSet rs) throws SQLException {
        int id = rs.getInt("Id");
        String plate = rs.getString("Plate");
        int IdCategory = rs.getInt("IdCategory");
        int IdTypeVehicle = rs.getInt("IdTypeVehicle");

        Categories category;
        switch (IdCategory) {
            case 1:
                category = Categories.MENSALISTA;
                break;
            case 2:
                category = Categories.CAMINHAO;
                break;
            case 3:
                category = Categories.SERVICO_PUBLICO;
                break;
            case 4:
                category = Categories.AVULSO;
                break;
            default:
                throw new DbException("Categoria de veículo desconhecida: " + IdCategory);
        }
        TypeVehicle typeVehicle;
        switch (IdTypeVehicle) {
            case 1:
                typeVehicle = TypeVehicle.CARRO;
                break;
            case 2:
                typeVehicle = TypeVehicle.MOTO;
                break;
            case 3:
                typeVehicle = TypeVehicle.CAMINHAO;
                break;
            default:
                throw new DbException("Tipo de veículo desconhecido: " + IdTypeVehicle);
        }

        if (IdCategory == 1 || IdCategory == 4) {
            switch (IdTypeVehicle) {
                case 1:
                    return new Car(id, plate, category, typeVehicle);
                case 2:
                    return new Motorcycle(id, plate, category, typeVehicle);
                default:
                    throw new DbException("Tipo de veículo desconhecido para a categoria: " + IdTypeVehicle);
            }
        } else if (IdCategory == 2) {
            return new Truck(id, plate, category);
        } else {
            return new PublicService(id, category);
        }
    }

    @Override
    public Vehicle findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT vehicle.Id, vehicle.Plate, vehicle.IdCategory, vehicle.IdTypeVehicle, category.Name AS category " +
                            "FROM vehicle " +
                            "INNER JOIN category " +
                            "ON vehicle.IdCategory = category.Id " +
                            "WHERE vehicle.Id = ?;");
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                return instantiateVehicle(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public Vehicle findByPlate(String plate) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT * FROM vehicle WHERE Plate = ?;");
            st.setString(1, plate);
            rs = st.executeQuery();

            if (rs.next()) {
                return instantiateVehicle(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Vehicle> findAll() {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT * " +
                            "FROM vehicle " +
                            "ORDER BY IdCategory;");
            rs = st.executeQuery();

            List<Vehicle> list = new ArrayList<>();
            while (rs.next()) {
                Vehicle vehicle = instantiateVehicle(rs);
                list.add(vehicle);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
