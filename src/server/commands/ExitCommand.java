package server.commands;

import connect_utils.CommandInfo;
import server.Logger;
import server.connection_control.User;

import java.util.logging.Level;

public class ExitCommand extends Command {
    ExitCommand() {
        super("exit", "", "завершает выполнение программы", CommandInfo.SendInfo.EXIT,
                null, false);
    }

    /**
     * Close connection with user
     *
     * @param user              that execute this command
     * @param programController that execute this command
     * @param args              of command
     * @return null
     */
    @Override
    public String execute(User user, ProgramController programController, String[] args) {
        user.disconnect();
        Logger.getLogger().log(Level.INFO, "Пользователь " +
                (user.getLogin() == null ? user.getAddress() : user.getLogin()) + " отключился от сервера.");
        return null;
    }
}
