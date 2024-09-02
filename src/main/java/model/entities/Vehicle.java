package model.entities;

import model.enums.Categories;
import model.enums.TypeVehicle;

import java.io.Serializable;

public abstract class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Gate gate;
    private Vacancy vacancy;
    private String plate;
    private Categories category;
    private Double payment;
    private Management management;

    public Vehicle() {
    }

    public Vehicle(Integer id, Gate gate, Vacancy vacancy, String plate, Categories category, Double payment) {
        this.id = id;
        this.gate = gate;
        this.vacancy = vacancy;
        this.plate = plate;
        this.category = category;
        this.payment = payment;
    }

    public int getId() {
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

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    public Management getManagement() {
        return management;
    }

    public void setManagement(Management management) {
        this.management = management;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment() {
        System.out.println("Category: " + getCategory());

        if (getCategory().equals(Categories.MENSALISTA)) {
            this.payment = 250.0;
            System.out.println("Mensalista payment: " + this.payment);
        } else if (getCategory().equals(Categories.SERVICO_PUBLICO) || getCategory().equals(Categories.CAMINHAO)) {
            this.payment = 0.0;
            System.out.println("Servico Publico/Caminhao payment: " + this.payment);
        } else {
            int occupiedVacancies = getRequiredVacancies();
            long duration = management.getDuration();
            this.payment = duration * 0.10 * occupiedVacancies;

            System.out.println("Duration: " + duration + ", Vacancies: " + occupiedVacancies + ", Payment: " + this.payment);

            if (this.payment < 5.0) {
                this.payment = 5.0;
            }
        }
        System.out.println("Final Payment: " + this.payment);
    }

    public abstract Integer getRequiredVacancies();
    public abstract TypeVehicle getTypeVehicle();
}
