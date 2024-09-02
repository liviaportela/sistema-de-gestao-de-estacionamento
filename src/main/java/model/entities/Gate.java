package model.entities;

import model.dao.VehicleDao;
import model.enums.Categories;
import model.enums.TypeVehicle;
import model.exceptions.GateException;

import java.io.Serializable;
import java.util.Objects;
import java.util.Scanner;

public class Gate implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer entranceGate;
    private Integer exitGate;

    public Gate() {
    }

    public Gate(Integer id, Integer entranceGate, Integer exitGate) {
        this.id = id;
        this.entranceGate = entranceGate;
        this.exitGate = exitGate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEntranceGate() {
        return entranceGate;
    }

    public void setEntranceGate(Integer entranceGate) {
        this.entranceGate = entranceGate;
    }

    public Integer getExitGate() {
        return exitGate;
    }

    public void setExitGate(Integer exitGate) {
        this.exitGate = exitGate;
    }

    public static boolean validateEntry(Categories category, TypeVehicle typeVehicle, Integer entranceGate) {
        return verifyGateEntry(category, typeVehicle, entranceGate);
    }

    public static boolean validateExit(Categories category, TypeVehicle typeVehicle, Integer exitGate) {
        return verifyGateExit(category, typeVehicle, exitGate);
    }

    public static boolean verifyGateEntry(Categories category, TypeVehicle typeVehicle, Integer entranceGate) {
        if (entranceGate < 1 || entranceGate > 5) {
            throw new GateException("Erro: A cancela " + entranceGate + " não é válida para entrada.");
        }

        if (category == Categories.CAMINHAO) {
            if (entranceGate != 1) {
                throw new GateException("Erro: Caminhões só podem entrar pelo portão 1.");
            }
            else return true;
        }

        if (category == Categories.MENSALISTA || category == Categories.AVULSO) {
            if (typeVehicle == TypeVehicle.MOTO) {
                if (entranceGate != 5) {
                    throw new GateException("Erro: Motos só podem entrar pelo portão 5.");
                }
            }
            else return true;
        }
        return false;
    }

    public static boolean verifyGateExit(Categories category, TypeVehicle typeVehicle, Integer exitGate) {
        if (exitGate < 6 || exitGate > 10) {
            throw new GateException("Erro: A cancela " + exitGate + " não é válida para saída.");
        }

        if (category == Categories.MENSALISTA || category == Categories.AVULSO) {
            if (typeVehicle == TypeVehicle.MOTO){
                if (exitGate != 10) {
                    throw new GateException("Erro: Motos só podem sair pelo portão 10.");
                }
            }
            else return true;
        }
        return false;
    }

    public void validateRegistered(Vehicle vehicle, Categories category) {
        if (vehicle == null) {
            System.out.println("Veículo não encontrado.");
            return;
        }

        if (!vehicle.getCategory().equals(category)) {
            System.out.println("A categoria do veículo não corresponde à categoria registrada.");
        }
    }

    @Override
    public String toString() {
        return "entranceGate: " + entranceGate +
                " exitGate: " + exitGate;
    }
}
