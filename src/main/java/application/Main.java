package application;

import model.dao.DaoFactory;
import model.dao.ManagementDao;
import model.dao.VehicleDao;
import model.entities.veiculos.Car;
import model.entities.veiculos.Motorcycle;
import model.entities.veiculos.Truck;
import model.enums.Categories;
import model.exceptions.ManagementException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        VehicleDao vehicleDao = DaoFactory.createVeiculoDao();

        try {
            System.out.println("Sistema de Gerenciamento de Estacionamento\n");
            System.out.println("========== Cadastro de veículos ==========");
            System.out.print("Tipo de veículo (mensalista, caminhao, servico_publico, avulso): ");
            Categories category = Categories.valueOf(sc.next().toUpperCase());
            if (category.equals(Categories.MENSALISTA) || category.equals(Categories.CAMINHAO)) {
                System.out.print("Placa: ");
                String plate = sc.next();
                if (category.equals(Categories.MENSALISTA)) {
                    System.out.print("Tipo de veículo (carro, moto): ");
                    String monthlyType = sc.next().toUpperCase();
                    if (monthlyType.equals("CARRO")) {
                        Car newCar = new Car(plate, Categories.MENSALISTA);
                        vehicleDao.insert(newCar);
                    } else if (monthlyType.equals("MOTO")) {
                        Motorcycle newMotorcycle = new Motorcycle(plate, Categories.MENSALISTA);
                        vehicleDao.insert(newMotorcycle);
                    }
                } else {
                    Truck newTruck = new Truck(plate, Categories.CAMINHAO);
                    vehicleDao.insert(newTruck);
                }
                System.out.println("O veículo foi cadastrado com sucesso!\n");
            } else {
                throw new ManagementException("Esse tipo de veículo não pode ser cadastrado!");
            }
        } catch (IllegalArgumentException e) {
            throw new ManagementException(e.getMessage());
        } finally {
            sc.close();
        }
    }
}