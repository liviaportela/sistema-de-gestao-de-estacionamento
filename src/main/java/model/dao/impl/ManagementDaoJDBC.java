package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.ManagementDao;
import model.dao.VacancyDao;
import model.entities.Management;
import model.entities.Vacancy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManagementDaoJDBC implements ManagementDao {

    private Connection conn;
    private VacancyDao vacancyDao;

    public ManagementDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Management obj) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO management (Entry, IdEntranceGate, IdVehicle) " +
                            "VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setTimestamp(1, Timestamp.valueOf(obj.getEntryDate()));
            st.setInt(2, obj.getGate().getEntranceGate());
            st.setInt(3, obj.getVehicle().getId());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    obj.setId(generatedId);
                }
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Management obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE management " +
                            "SET Departure = ? " +
                            "IdExitGate = ? " +
                            "Value = ? " +
                            "WHERE Id = ?");
            st.setTimestamp(1, Timestamp.valueOf(obj.getDepartureDate()));
            st.setInt(2, obj.getGate().getExitGate());
            st.setDouble(3, obj.getVehicle().getPayment());
            st.setInt(4, obj.getId());

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
            st = conn.prepareStatement(
                    "DELETE FROM management " +
                            "WHERE Id = ?");
            st.setInt(1, id);
            int rows = st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Management findById(Integer id) {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * " +
                    "FROM management " +
                    "WHERE Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                return instantiateManagement(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Management instantiateManagement(ResultSet rs) throws SQLException {
        Management management = new Management();
        management.setId(rs.getInt("Id"));
        return management;
    }

    @Override
    public List<Management> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT * " +
                            "FROM management " +
                            "ORDER BY Name");
            rs = st.executeQuery();

            List<Management> list = new ArrayList<>();
            while (rs.next()) {
                Management management = instantiateManagement(rs);
                list.add(management);
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
