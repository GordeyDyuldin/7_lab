package client;

import client.commands.CommandController;
import exceptions.ConfigFileNotFoundException;
import exceptions.ConnectionException;
import exceptions.MissingArgumentException;

public class Main {
    /**
     * Start execution of program
     *
     * @param args do not use
     */
    public static void main(String[] args) {
        CommandController cc;
        try {
            cc = new CommandController();
        } catch (MissingArgumentException e) {
            System.out.println("Ошибка в файле конфигурации: " + e.getMessage());
            return;
        } catch (ConfigFileNotFoundException | ConnectionException e) {
            System.out.println(e.getMessage());
            return;
        }
        try {
            cc.connect();
        } catch (ConnectionException e) {
            System.out.println(e.getMessage());
        }
    }
}
