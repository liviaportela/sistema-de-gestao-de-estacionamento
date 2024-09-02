package application;

import model.dao.DaoFactory;
import model.dao.VacancyDao;
import model.exceptions.ManagementException;
import model.services.Menu;

import java.util.Scanner;

import static model.entities.Vacancy.*;

public class Main {
    public static void main(String[] args) {

        VacancyDao vacancyDao = DaoFactory.createVacancyDao();
        createVacancies(vacancyDao);
        Menu menu = new Menu();

        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Menu: 1.Registro de veículos; 2.Entrada de veículos; 3.Saída de veículos; 4.Sair" );
            int menuN = sc.nextInt();

            while (menuN != 4) {
                switch (menuN) {
                    case 1:
                        menu.vehicleRegistration(sc);
                        break;
                    case 2:
                        menu.vehicleEntry(sc);
                        break;
                    case 3:
                        menu.vehicleExit(sc);
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        break;
                }
                System.out.print("Digite a próxima opção: ");
                menuN = sc.nextInt();
            }
            System.out.println("Programa encerrado.");

        } catch (IllegalArgumentException e) {
            throw new ManagementException(e.getMessage());
        }
    }
}