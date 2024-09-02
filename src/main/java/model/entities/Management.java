package model.entities;

import model.dao.ManagementDao;
import model.dao.VacancyDao;
import model.enums.Categories;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Management implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Gate gate;
    private Vacancy vacancy;
    private List<Vacancy> vacancies;
    private Vehicle vehicle;
    private LocalDateTime entryDate;
    private LocalDateTime departureDate;

    public Management() {
    }

    public Management(Gate gate, Vehicle vehicle) {
        this.setGate(gate);
        this.setVehicle(vehicle);
    }

    public Management(Gate gate, LocalDateTime entryDate, Vehicle vehicle, List<Vacancy> vacancies) {
        this.setGate(gate);
        this.setEntryDate(entryDate);
        this.setVehicle(vehicle);
        vehicle.setManagement(this);
        this.vacancies = new ArrayList<>(vacancies);
    }

    public Management(Gate gate, LocalDateTime departureDate, Vehicle vehicle) {
        this.setGate(gate);
        this.setDepartureDate(departureDate);
        this.setVehicle(vehicle);
        vehicle.setManagement(this);
    }

    public Management(Integer id, Gate gate, Vacancy vacancy, Vehicle vehicle, LocalDateTime entryDate, LocalDateTime departureDate) {
        this.id = id;
        this.gate = gate;
        this.vacancy = vacancy;
        this.vehicle = vehicle;
        this.entryDate = entryDate;
        this.departureDate = departureDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Gate getGate() {
        return gate;
    }

    public void setGate(Gate gate) {
        this.gate = gate;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    public List<Vacancy> getVacancies() {
        return vacancies;
    }

    public void setVacancies(List<Vacancy> vacancies) {
        this.vacancies = vacancies;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDateTime entryDate) {
        this.entryDate = entryDate;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public long getDuration() {
        Duration duration = Duration.between(entryDate, departureDate);
        return duration.toMinutes();
    }

    public List<Vacancy> findVacanciesByVehicleId(Integer vehicleId) {
        if (vehicleId == null) {
            System.out.println("Vehicle ID is null!");
            return Collections.emptyList();
        }

        return vacancies.stream()
                .filter(vacancy -> vacancy.getVehicle() != null && vacancy.getVehicle().getId() == vehicleId)
                .collect(Collectors.toList());
    }

    public static void releaseSpaces(VacancyDao vacancyDao, int vehicleId) {
        List<Vacancy> occupiedVacancies = vacancyDao.findByVehicleId(vehicleId);

        if (occupiedVacancies != null) {
            for (Vacancy vacancy : occupiedVacancies) {
                if (vacancy != null) {
                    vacancy.setAvailable(true);
                    vacancy.setVehicle(null);
                    vacancyDao.update(vacancy);
                }
            }
        }
    }

    public void finalizeExit(LocalDateTime departure, Integer exitGate, VacancyDao vacancyDao, ManagementDao managementDao) {
        setDepartureDate(departure);
        getGate().setExitGate(exitGate);

        Vehicle vehicle = getVehicle();
        if (vehicle != null) {
            int vehicleId = vehicle.getId();

            releaseSpaces(vacancyDao, vehicleId);
            managementDao.update(this);

            if (vehicle.getCategory().equals(Categories.AVULSO)) {
                System.out.println(getTicket());
            }

            if (vehicle.getCategory().equals(Categories.MENSALISTA)) {
                System.out.println(getVehicle().getPayment());
            }
        }
    }

    public String getTicket() {
        return "======= TICKET =======" +
                "Data e hora de entrada: " + getEntryDate() +
                " Data e hora de saída: " + getDepartureDate() +
                " Cancela de entrada: " + getGate().getEntranceGate() +
                " Cancela de saída: " + getGate().getExitGate() +
                " Vaga(s) ocupada(s): " + findVacanciesByVehicleId(vehicle.getId()) +
                " Valor a ser pago: " + vehicle.getPayment();
    }

    @Override
    public String toString() {
        return "Management{" +
                "id=" + id +
                ", vehicle=" + vehicle +
                ", entryDate=" + entryDate +
                ", departureDate=" + departureDate +
                '}';
    }
}
