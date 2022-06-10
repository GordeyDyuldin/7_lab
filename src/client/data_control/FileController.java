package client.data_control;

import client.commands.CommandController;
import connect_utils.CommandInfo;
import exceptions.ConfigFileNotFoundException;
import exceptions.MissingArgumentException;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Control data reading from file
 */
public class FileController {
    /**
     * Current program controller
     */
    private final CommandController commandController;

    /**
     * LAST from reading script file commands with args as string
     */
    private ArrayList<String> strCommand;

    public FileController(CommandController commandController) {
        this.commandController = commandController;
    }

    /**
     * Read script file with commands on new lines
     *
     * @param path of file with script
     * @return ArrayList of CommandInfo from script
     * @throws FileNotFoundException if script file not found
     */
    public ArrayList<CommandInfo> readScriptFile(String path) throws FileNotFoundException {
        strCommand = new ArrayList<>();
        ArrayList<CommandInfo> commandsInfo = new ArrayList<>();
        BufferedReader buffIn = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        buffIn.lines().forEach(string -> {
            String[] args;
            String s = string.replaceAll(" +", " ");
            args = s.split(" ");
            if (commandController.isValidCommand(args)) {
                commandsInfo.add(commandController.parseCommand(args[0]));
                strCommand.add(string);
            }
        });
        return commandsInfo;
    }

    /**
     * Read config file with "config.excalibbur".
     * File need to have "port: *digits*" and "address: *IP address/domain*"
     *
     * @throws MissingArgumentException if port or address not found in file
     */
    public InetSocketAddress readConfig() throws MissingArgumentException, ConfigFileNotFoundException {
        Scanner scanner;
        try {
            scanner = new Scanner(new FileInputStream("config.excalibbur"));
        } catch (FileNotFoundException e) {
            throw new ConfigFileNotFoundException("Не найден файл конфигурации. " +
                    "Создайте файл конфигурации config.excalibbur\n" +
                    "И добавьте в него строки \"address: localhost\" (допускается обычный ip, сервера)" +
                    " и \"port: 1234\" (порт сервера)");
        }
        StringBuilder s = new StringBuilder();
        int port;
        String address;
        while (scanner.hasNextLine())
            s.append(scanner.nextLine()).append("\n");
        Matcher matcher = Pattern.compile("(?<=port:)\\d+|(?<=port:\\s)\\d+|(?<=port:\\s{2})\\d+",
                Pattern.CASE_INSENSITIVE).matcher(s.toString());
        scanner.close();
        if (matcher.find())
            port = Integer.parseInt(s.substring(matcher.start(), matcher.end()));
        else
            throw new MissingArgumentException("в файле конфигурации не был найден порт. " +
                    "Добавьте в файл строку типа \"port: 1234\"");
        matcher = Pattern.compile("(?<=address:)[\\w\\d.]+|(?<=address:\\s)[\\w\\d.]+|(?<=address:\\s{2})[\\w\\d.]+",
                Pattern.CASE_INSENSITIVE).matcher(s.toString());
        if (matcher.find())
            address = s.substring(matcher.start(), matcher.end());
        else
            throw new MissingArgumentException("в файле конфигурации не был найден адрес сервера. " +
                    "Добавьте в файл строку типа\n" +
                    "\"address: localhost\", \"address: 192.65.3.5\"");
        return new InetSocketAddress(address, port);
    }

    public ArrayList<String> getStrCommand() {
        return strCommand;
    }
}
