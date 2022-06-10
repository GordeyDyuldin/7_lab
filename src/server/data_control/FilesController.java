package server.data_control;

import exceptions.ConfigFileNotFoundException;
import exceptions.MissingArgumentException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class uses for read configuration files
 */
public class FilesController {
    /**
     * Read database password from file database.config and return it
     *
     * @return password from file
     * @throws ConfigFileNotFoundException if file does not exist
     */
    public static String readDBPassword() throws ConfigFileNotFoundException {
        Scanner scanner;
        try {
            scanner = new Scanner(new FileInputStream("database.config"));
        } catch (FileNotFoundException e) {
            throw new ConfigFileNotFoundException("Не был найден файл конфигурации базы данных database.config.");
        }
        StringBuilder s = new StringBuilder();
        while (scanner.hasNextLine()) {
            s.append(scanner.nextLine()).append("\n");
        }
        scanner.close();
        Matcher matcher = Pattern.compile("(?<=password:\\s{0,10})[^\\s]+", Pattern.CASE_INSENSITIVE)
                .matcher(s.toString());
        if (matcher.find())
            return s.substring(matcher.start(), matcher.end());
        else
            return "";
    }

    /**
     * Read config file config.excalibbur for connection
     *
     * @return port where need to create serverSocket
     * @throws MissingArgumentException    if port does not find in file
     * @throws ConfigFileNotFoundException if file does not find
     */
    public static int readConfigPort() throws MissingArgumentException, ConfigFileNotFoundException {
        Scanner scanner;
        try {
            scanner = new Scanner(new FileInputStream("config.excalibbur"));
        } catch (FileNotFoundException e) {
            throw new ConfigFileNotFoundException("Не был найден файл конфигурации config.excalibbur. " +
                    "Добавьте его, указав порт следующим образом:\n" +
                    "port: 1234");
        }
        StringBuilder s = new StringBuilder();
        while (scanner.hasNextLine())
            s.append(scanner.nextLine()).append("\n");
        scanner.close();
        Matcher matcher = Pattern.compile("(?<=port:)\\d+|(?<=port:\\s)\\d+|(?<=port:\\s{2})\\d+",
                Pattern.CASE_INSENSITIVE).matcher(s.toString());
        if (matcher.find())
            return Integer.parseInt(s.substring(matcher.start(), matcher.end()));
        else
            throw new MissingArgumentException("в файле конфигурации подключения не был найден порт. " +
                    "Добавьте в файл строку типа \"port: 1234\"");
    }
}
