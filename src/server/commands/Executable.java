package server.commands;

import exceptions.IncorrectArgumentException;
import server.commands.ProgramController;
import server.connection_control.User;

import java.io.IOException;

/**
 * interface for all commands
 * <p>Uses for execute command</p>
 */
public interface Executable {
    /**
     * Execute this command
     *
     * @param user              that execute this command
     * @param programController that execute this command
     * @param args              of command
     * @return reply that need to send or null
     * @throws IncorrectArgumentException if args do not correct
     * @throws IOException                if connection is closed
     * @throws ClassNotFoundException     if receiving information is not expected
     */
    String execute(User user, ProgramController programController, String[] args) throws IncorrectArgumentException,
            IOException, ClassNotFoundException;
}
