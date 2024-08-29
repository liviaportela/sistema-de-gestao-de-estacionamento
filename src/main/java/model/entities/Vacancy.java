package model.entities;

import model.exceptions.ManagementException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Vacancy {

    private List<Vacancy> vacancy;
    private Integer id;
    private LocalDateTime entryDate;
    private LocalDateTime departureDate;

    public Vacancy() {
    }

    public Vacancy(Integer id, LocalDateTime entryDate, LocalDateTime departureDate) {
        if (!departureDate.isAfter(entryDate)) {
            throw new ManagementException("A data de saída deve ser após a data de entrada.");
        }
        this.id = id;
        this.entryDate = entryDate;
        this.departureDate = departureDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public long duration() {
        Duration duration = Duration.between(departureDate, entryDate);
        return duration.toMillis();
    }

    public List<Vacancy> getVacancy() {
        return vacancy;
    }

    public void setVacancy(List<Vacancy> vacancy) {
        this.vacancy = vacancy;
    }
}
