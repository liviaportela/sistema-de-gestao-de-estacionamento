package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.VacancyDao;
import model.entities.Vacancy;
import model.entities.Vehicle;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VacancyDaoJDBC implements VacancyDao {

    private Connection conn;

    public VacancyDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Vacancy obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO vacancy" +
                            "(Available) " +
                            "VALUES " +
                            "(?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setBoolean(1, obj.getAvailable());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    obj.setId(rs.getInt(1));
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
    public void update(Vacancy obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE vacancy " +
                            "SET Available = ?, " +
                            "IdVehicle = ? " +
                            "WHERE Id = ?");
            st.setBoolean(1, obj.getAvailable());
            st.setInt(2, obj.getVehicle().getId());
            st.setInt(3, obj.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    private Vacancy instantiateVacancy(ResultSet rs) throws SQLException {
        Vacancy vacancy = new Vacancy();
        vacancy.setId(rs.getInt("Id"));
        vacancy.setAvailable(rs.getBoolean("Available"));
        return vacancy;
    }

    @Override
    public List<Vacancy> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT * " +
                            "FROM vacancy");
            rs = st.executeQuery();

            List<Vacancy> list = new ArrayList<>();
            while (rs.next()) {
                Vacancy vacancy = instantiateVacancy(rs);
                list.add(vacancy);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Vacancy> findByVehicleId(Integer vehicleId) {
        List<Vacancy> vacancies = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT * FROM vacancy WHERE IdVehicle = ?");
            st.setInt(1, vehicleId);
            rs = st.executeQuery();

            while (rs.next()) {
                Vacancy vacancy = new Vacancy();
                vacancy.setId(rs.getInt("id"));
                vacancy.setAvailable(rs.getBoolean("available"));
                vacancies.add(vacancy);
            }

        } catch (SQLException e) {
            throw new DbException("Error fetching vacancies for Vehicle ID: " + vehicleId + ". " + e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }

        return vacancies;
    }

    public void releaseVacancies(int managementId) {
        try (PreparedStatement st = conn.prepareStatement("UPDATE management SET Departure = ? WHERE Id = ?")) {
            st.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            st.setInt(2, managementId);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public boolean allocateVacancies(Integer vehicleId, int requiredVacancies) {
        PreparedStatement st = null;

        List<Integer> vacancyIds = findAvailableVacancyIds(requiredVacancies);
        if (vacancyIds.size() < requiredVacancies) {
            return false;
        }
        try {
            st = conn.prepareStatement(
                    "UPDATE vacancy " +
                            "SET Avaiable = ? " +
                            "IdVehicle = ?" +
                            "WHERE Id = ?");
            for (Integer vacancyId : vacancyIds) {
                st.setBoolean(1, false);
                st.setInt(2, vehicleId);
                st.setInt(3, vacancyId);
                st.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public boolean releaseVacancies(Integer vehicleId) {
        PreparedStatement st = null;
        List<Integer> vacancyIds = findOccupiedVacancyIds(vehicleId);
        if (vacancyIds.isEmpty()) {
            return false;
        }
        try {
            st = conn.prepareStatement(
                    "UPDATE vacancy " +
                            "SET Avaiable = ? " +
                            "IdVehicle = ?" +
                            "WHERE Id = ?");
            for (Integer vacancyId : vacancyIds) {
                st.setBoolean(1, true);
                st.setNull(2, java.sql.Types.INTEGER);
                st.setInt(3, vacancyId);
                st.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    private List<Integer> findAvailableVacancyIds(int requiredVacancies) {
        List<Integer> vacancyIds = new ArrayList<>();

        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT Id " +
                    "FROM vacancy " +
                    "WHERE Available = true LIMIT ?");
            st.setInt(1, requiredVacancies);
            rs = st.executeQuery();
            while (rs.next()) {
                vacancyIds.add(rs.getInt("Id"));
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
        return vacancyIds;
    }

    private List<Integer> findOccupiedVacancyIds(Integer vehicleId) {
        List<Integer> vacancyIds = new ArrayList<>();

        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT Id " +
                    "FROM vacancy " +
                    "WHERE IdVehicle = ?");
            st.setInt(1, vehicleId);
            rs = st.executeQuery();
            while (rs.next()) {
                vacancyIds.add(rs.getInt("Id"));
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
        return vacancyIds;
    }
}
