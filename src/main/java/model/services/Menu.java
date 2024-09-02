package model.services;

import model.dao.DaoFactory;
import model.dao.ManagementDao;
import model.dao.VacancyDao;
import model.dao.VehicleDao;
import model.entities.Gate;
import model.entities.Management;
import model.entities.Vacancy;
import model.entities.Vehicle;
import model.entities.vehicles.Car;
import model.entities.vehicles.Motorcycle;
import model.entities.vehicles.PublicService;
import model.entities.vehicles.Truck;
import model.enums.Categories;
import model.enums.TypeVehicle;
import model.exceptions.ManagementException;
import model.exceptions.VacancyException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Menu {

    VehicleDao vehicleDao = DaoFactory.createVehicleDao();
    ManagementDao managementDao = DaoFactory.createManagementDao();
    VacancyDao vacancyDao = DaoFactory.createVacancyDao();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public void vehicleRegistration(Scanner sc) {
        try {
            System.out.println("Sistema de Gerenciamento de Estacionamento\n");
            System.out.println("========== Cadastro de veículos ==========");
            System.out.print("Tipo de veículo (mensalista, caminhao, servico_publico, avulso): ");
            Categories categories = Categories.valueOf(sc.next().toUpperCase());
            if (categories.equals(Categories.MENSALISTA) || categories.equals(Categories.CAMINHAO)) {
                System.out.print("Placa: ");
                String plate = sc.next();
                if (categories.equals(Categories.MENSALISTA)) {
                    System.out.print("Tipo de veículo (carro, moto): ");
                    TypeVehicle typeVehicle = TypeVehicle.valueOf(sc.next().toUpperCase());
                    if (typeVehicle.equals(TypeVehicle.CARRO)) {
                        vehicleDao.insert(new Car(plate, Categories.MENSALISTA, TypeVehicle.CARRO));
                    } else if (typeVehicle.equals(TypeVehicle.MOTO)) {
                        vehicleDao.insert(new Motorcycle(plate, Categories.MENSALISTA, TypeVehicle.MOTO));
                    }
                } else {
                    vehicleDao.insert(new Truck(plate, Categories.CAMINHAO));
                }
                System.out.println("O veículo foi cadastrado com sucesso!\n");
            } else {
                throw new ManagementException("Esse tipo de veículo não pode ser cadastrado!");
            }
        } catch (ManagementException e) {
            throw new ManagementException(e.getMessage());
        }
    }

    public void vehicleEntry(Scanner sc) {
        try {
            System.out.println("======== Gerenciamento de entrada ========");
            System.out.print("Tipo de veículo (mensalista, caminhao, servico_publico, avulso): ");
            Categories category = Categories.valueOf(sc.next().toUpperCase());
            System.out.print("Cancela de entrada (1 - 5): ");
            Integer entryGate = sc.nextInt();

            TypeVehicle typeVehicle;
            Vehicle vehicle;
            Gate gate = new Gate();
            gate.setEntranceGate(entryGate);
            if (category.equals(Categories.MENSALISTA) || category.equals(Categories.AVULSO)) {
                System.out.print("Tipo de veículo (carro, moto): ");
                typeVehicle = TypeVehicle.valueOf(sc.next().toUpperCase());

                if (Gate.validateEntry(category, typeVehicle, entryGate)) {
                    LocalDateTime entry;
                    if (category.equals(Categories.AVULSO)) {
                        System.out.println("Data e hora de entrada (dd/MM/yyyy HH:mm:ss): ");
                        sc.nextLine();
                        String inputDateTime = sc.nextLine().trim();
                        if (inputDateTime.isEmpty()) {
                            throw new VacancyException("A entrada não pode estar vazia.");
                        } else {
                            if (typeVehicle.equals(TypeVehicle.CARRO)) {
                                Car car = new Car(Categories.AVULSO, TypeVehicle.CARRO);
                                vehicleDao.insert(car);
                                List<Vacancy> allocatedVacancies = Vacancy.allocateVacancies(vacancyDao, car);
                                entry = LocalDateTime.parse(inputDateTime, dtf);
                                Management management = new Management(gate, entry, car, allocatedVacancies);
                                managementDao.insert(management);
                                System.out.println("O veículo de id " + car.getId() + " estacionou na(s) vaga(s): " + management.getVacancies());
                            } else if (typeVehicle.equals(TypeVehicle.MOTO)) {
                                Motorcycle motorcycle = new Motorcycle(Categories.AVULSO, TypeVehicle.MOTO);
                                vehicleDao.insert(motorcycle);
                                List<Vacancy> allocatedVacancies = Vacancy.allocateVacancies(vacancyDao, motorcycle);
                                entry = LocalDateTime.parse(inputDateTime, dtf);
                                Management management = new Management(gate, entry, motorcycle, allocatedVacancies);
                                managementDao.insert(management);
                                System.out.println("O veículo de id " + motorcycle.getId() + " estacionou na(s) vaga(s): " + management.getVacancies());
                            }
                        }
                    }
                }
            }
            if (category.equals(Categories.MENSALISTA) || category.equals(Categories.CAMINHAO)) {
                System.out.print("Placa: ");
                String plate = sc.next();
                vehicle = vehicleDao.findByPlate(plate);
                if (vehicle != null) {
                    gate.validateRegistered(vehicle, category);
                    if (Gate.validateEntry(category, vehicle.getTypeVehicle(), entryGate)) {
                        Vacancy.allocateVacancies(vacancyDao, vehicle);
                        List<Vacancy> allocatedVacancies = Vacancy.allocateVacancies(vacancyDao, vehicle);
                        Management management = new Management(gate, LocalDateTime.now(), vehicle, allocatedVacancies);
                        managementDao.insert(management);
                        System.out.println("O veículo de id " + vehicle.getId() + " estacionou na(s) vaga(s): " + management.getVacancies());
                    } else {
                        System.out.println("A entrada pela cancela " + entryGate + " não é permitida para esse tipo de veículo.");
                    }
                }
            }
            if (category.equals(Categories.SERVICO_PUBLICO)) {
                PublicService publicService = new PublicService(Categories.SERVICO_PUBLICO);
                vehicleDao.insert(publicService);
                List<Vacancy> allocatedVacancies = Vacancy.allocateVacancies(vacancyDao, publicService);
                Management management = new Management(gate, LocalDateTime.now(), publicService, allocatedVacancies);
                managementDao.insert(management);
                System.out.println("O veículo de Id " + publicService.getId() + " entrou no estacionamento.");
            }
        } catch (ManagementException e) {
            throw new ManagementException(e.getMessage());
        }
    }

    public void vehicleExit(Scanner sc) {
        try {
            System.out.println("======== GERENCIAMENTO DA SAÍDA ========");
            System.out.print("Tipo de veículo (mensalista, caminhao, servico_publico, avulso): ");
            Categories category = Categories.valueOf(sc.next().toUpperCase());
            System.out.print("Cancela de saída (6 - 10): ");
            Integer exitGate = sc.nextInt();
            System.out.print("Id do veículo: ");
            int vehicleId = sc.nextInt();

            Management management = new Management();
            Gate gate = new Gate();
            gate.setExitGate(exitGate);
            if (category.equals(Categories.MENSALISTA) || category.equals(Categories.AVULSO)) {
                System.out.print("Tipo de veículo (carro, moto): ");
                TypeVehicle typeVehicle = TypeVehicle.valueOf(sc.next().toUpperCase());

                if (Gate.validateExit(category, typeVehicle, exitGate)) {
                    LocalDateTime departure;
                    if (category.equals(Categories.AVULSO)) {
                        System.out.println("Data e hora de saída (dd/MM/yyyy HH:mm:ss): ");
                        sc.nextLine();
                        String inputDateTime = sc.nextLine().trim();

                        if (inputDateTime.isEmpty()) {
                            throw new VacancyException("A saída não pode estar vazia.");
                        }
                        departure = LocalDateTime.parse(inputDateTime, dtf);
                    } else {
                        departure = LocalDateTime.now();
                    }
                    Vehicle vehicle = vehicleDao.findById(vehicleId);
                    management.setVehicle(vehicle);
                    management = new Management(gate, departure, vehicle);
                    if (vehicle != null) {
                        Management.releaseSpaces(vacancyDao, vehicle.getId());
                        management.finalizeExit(departure, exitGate, vacancyDao, managementDao);
                    }
                    managementDao.update(management);
                }
            } else if (category.equals(Categories.CAMINHAO) || category.equals(Categories.SERVICO_PUBLICO)) {
                Vehicle vehicle = vehicleDao.findById(vehicleId);
                management = new Management(gate, LocalDateTime.now(), vehicle);
                managementDao.update(management);
                Management.releaseSpaces(vacancyDao, vehicleId);
                if (category.equals(Categories.SERVICO_PUBLICO)) {
                    management.finalizeExit(LocalDateTime.now(), exitGate, vacancyDao, managementDao);
                }
            }
        } catch (ManagementException e) {
            throw new ManagementException(e.getMessage());
        }
    }
}
