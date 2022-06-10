package connect_utils;

import exceptions.IncorrectArgumentException;
import exceptions.MissingArgumentException;

import java.io.Serializable;

/**
 * Information for client about commands that he can use
 */
public class CommandInfo implements Serializable {
    /**
     * Name of command
     */
    private final String name;

    /**
     * Information that need to send on execution of this command
     */
    private final SendInfo sendInfo;

    /**
     * List of arguments that need to check before sending
     */
    private final ArgumentInfo[] argInfo;

    /**
     * Create command information for client from existing commands
     *
     * @param name     of command
     * @param sendInfo is data that client need to send to server
     * @param argInfo  is arguments that client need to check before he'll send it to server
     */
    public CommandInfo(String name, SendInfo sendInfo, ArgumentInfo[] argInfo) {
        this.name = name;
        this.sendInfo = sendInfo;
        this.argInfo = argInfo;
    }

    /**
     * Validate id as argument in command
     *
     * @param arg of command that user typed
     * @throws IncorrectArgumentException if id is incorrect in args
     * @throws MissingArgumentException   if id is missing in args
     */
    public static void idValidator(String arg) throws IncorrectArgumentException, MissingArgumentException {
        long id;
        try {
            id = Long.parseLong(arg);
        } catch (NumberFormatException e) {
            throw new IncorrectArgumentException("id - целое число");
        }
        if (id <= 0)
            throw new IncorrectArgumentException("id - число больше 0");
    }

    public String getName() {
        return name;
    }

    public SendInfo getSendInfo() {
        return sendInfo;
    }

    public ArgumentInfo[] getArgInfo() {
        return argInfo;
    }

    /**
     * Types of argument for command
     */
    public enum ArgumentInfo {
        ID,
        FLOAT,
        INT,
        STRING,
        CLIMATE,
        GOVERNMENT
    }

    /**
     * Types of send info from client
     * city - client need to send city object
     * city_update - client need to send updating city object
     * commands - client need to send new commands
     * exit - client can interrupt program with this command
     */
    public enum SendInfo {
        CITY,
        CITY_UPDATE,
        COMMANDS,
        EXIT,
        AUTH
    }
}