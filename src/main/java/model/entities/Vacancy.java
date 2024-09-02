package model.entities;

import model.dao.ManagementDao;
import model.dao.VacancyDao;
import model.enums.Categories;
import model.exceptions.ManagementException;
import model.exceptions.VacancyException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Vacancy implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private LocalDateTime entryDate;
    private LocalDateTime departureDate;
    private Boolean available = true;
    private Vehicle vehicle;
    private List<Boolean> vacancies;
    private Integer nextAvailableIndex;
    private Map<Integer, Vehicle> occupiedVacancies;

    private VacancyDao vacancyDao;

    public Vacancy() {
    }

    public Vacancy(Integer id, LocalDateTime entryDate, LocalDateTime departureDate, Vehicle vehicle) {
        if (!departureDate.isAfter(entryDate)) {
            throw new ManagementException("A data de saída deve ser após a data de entrada.");
        }
        this.id = id;
        this.entryDate = entryDate;
        this.departureDate = departureDate;
        this.vehicle = vehicle;
    }

    public Vacancy(Boolean available) {
        this.available = available;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public boolean allocateVacancies(Vehicle vehicle) {
        int requiredVacancies = vehicle.getRequiredVacancies();
        if (hasSufficientSpace(requiredVacancies)) {
            for (int i = nextAvailableIndex; i < nextAvailableIndex + requiredVacancies; i++) {
                vacancies.set(i, false);
            }
            occupiedVacancies.put(vehicle.getId(), vehicle);
            nextAvailableIndex += requiredVacancies;
            return true;
        }
        return false;
    }

    private boolean hasSufficientSpace(int requiredSpaces) {
        int availableSpaces = 0;
        for (int i = nextAvailableIndex; i < vacancies.size(); i++) {
            if (vacancies.get(i)) {
                availableSpaces++;
                if (availableSpaces == requiredSpaces) {
                    return true;
                }
            } else {
                availableSpaces = 0;
            }
        }
        return false;
    }

    public void releaseSpace(Integer vehicleId) {
        Vehicle vehicle = occupiedVacancies.remove(vehicleId);
        if (vehicle != null) {
            int requiredVacancies = vehicle.getRequiredVacancies();
            int startIndex = findStartIndex(vehicleId, requiredVacancies);
            for (int i = startIndex; i < startIndex + requiredVacancies; i++) {
                vacancies.set(i, true);
            }
            nextAvailableIndex = Math.max(nextAvailableIndex - requiredVacancies, 0);
        }
    }

    private int findStartIndex(Integer vehicleId, int requiredVacancies) {
        int index = 0;
        for (Map.Entry<Integer, Vehicle> entry : occupiedVacancies.entrySet()) {
            if (entry.getKey().equals(vehicleId)) {
                break;
            }
            index += entry.getValue().getRequiredVacancies();
        }
        return index;
    }

    public static List<Vacancy> allocateVacancies(VacancyDao vacancyDao, Vehicle vehicle) {
        int requiredVacancies = vehicle.getRequiredVacancies();
        List<Vacancy> allVacancies = vacancyDao.findAll();
        List<Vacancy> availableVacancies = new ArrayList<>();
        List<Vacancy> allocatedVacancies = new ArrayList<>();

        if (vehicle.getCategory().equals(Categories.MENSALISTA)) {
            for (int i = 0; i < 200 && i < allVacancies.size(); i++) {
                if (allVacancies.get(i).getAvailable()) {
                    availableVacancies.add(allVacancies.get(i));
                }
            }
        } else if (vehicle.getCategory().equals(Categories.AVULSO) || vehicle.getCategory().equals(Categories.CAMINHAO)) {
            for (int i = 200; i < allVacancies.size(); i++) {
                if (allVacancies.get(i).getAvailable()) {
                    availableVacancies.add(allVacancies.get(i));
                }
            }
        }

        if (availableVacancies.size() < requiredVacancies) {
            return Collections.emptyList();
        }

        int count = 0;
        for (Vacancy vacancy : availableVacancies) {
            if (count >= requiredVacancies) break;
            vacancy.setAvailable(false);
            vacancy.setVehicle(vehicle);
            vacancyDao.update(vacancy);
            allocatedVacancies.add(vacancy);
            count++;
        }

        return allocatedVacancies;
    }

    public static void createVacancies(VacancyDao vacancyDao) {
        List<Vacancy> existingVacancies = vacancyDao.findAll();
        int currentVacancyCount = existingVacancies.size();

        if (currentVacancyCount < 500) {
            int vacanciesToCreate = 500 - currentVacancyCount;

            for (int i = 0; i < vacanciesToCreate; i++) {
                Vacancy newVacancy = new Vacancy(true);
                vacancyDao.insert(newVacancy);
            }
        }
    }

    @Override
    public String toString() {
        return "" + id;
    }
}
