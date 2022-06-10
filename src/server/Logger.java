package server;

import server.commands.ProgramController;
import server.connection_control.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * Logger for all classes
 */
public class Logger {
    /**
     * instance of logger
     */
    private static java.util.logging.Logger logger;

    /**
     * Create logger with config from file logger.config
     * Need to use when program start
     */
    public static void createLogger() {
        try (FileInputStream ins = new FileInputStream("logger.config")) {
            LogManager.getLogManager().readConfiguration(ins);
        } catch (FileNotFoundException e) {
            System.out.println("Файл конфигурации логгера не найден.");
        } catch (IOException e) {
            System.out.println("Не удалось открыть файл конфигурации логгера.");
        }
        logger = java.util.logging.Logger.getLogger(ProgramController.class.getName());
    }

    /**
     * Create log about disconnect of user
     *
     * @param user that was disconnected
     */
    public static void logDisconnect(User user) {
        Logger.getLogger().log(Level.WARNING,
                "Потеряно соединение с клиентом " + (user.getLogin() == null ? user.getAddress() : user.getLogin()));
    }

    /**
     * Get using logger (need to create logger before)
     *
     * @return login
     */
    public static java.util.logging.Logger getLogger() {
        return logger;
    }
}
