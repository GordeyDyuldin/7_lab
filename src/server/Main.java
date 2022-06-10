package server;

import exceptions.ConfigFileNotFoundException;
import exceptions.MissingArgumentException;
import server.commands.ProgramController;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;

public class Main {
    /**
     * Start program
     *
     * @param args is not using
     */
    public static void main(String[] args) {
        Logger.createLogger();
        final ProgramController programController;
        try {
            programController = new ProgramController();
        } catch (SQLException e) {
            Logger.getLogger().log(Level.WARNING, "Ошибка подключения к базе данных.");
            e.printStackTrace();
            return;
        } catch (MissingArgumentException e) {
            Logger.getLogger().log(Level.WARNING, "Не найдены обязательные данные в файлах: " + e.getMessage());
            return;
        } catch (ConfigFileNotFoundException e) {
            Logger.getLogger().log(Level.WARNING, e.getMessage());
            return;
        }
        Thread consoleListener = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String input;
            while (true) {
                input = scanner.nextLine().toLowerCase();
                if (input.equals("stop")) {
                    Logger.getLogger().log(Level.INFO, "Отключение сервера...");
                    programController.stop();
                    System.exit(0);
                } else
                    System.out.println("Незнакомая команда. Попробуйте stop");
            }
        }); // для остановки сервера
        consoleListener.start();
        programController.start();
    }
}
