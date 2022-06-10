package server.commands;

import connect_utils.CommandInfo;
import exceptions.IncorrectArgumentException;
import server.connection_control.User;
import server.data_control.PasswordManager;

import java.io.IOException;
import java.sql.SQLException;

public class LoginCommand extends Command {
    /**
     * Create new command that can execute on server
     */
    LoginCommand() {
        super("login", "username password", "выполняет авторизацию пользователя", null,
                new CommandInfo.ArgumentInfo[]{CommandInfo.ArgumentInfo.STRING, CommandInfo.ArgumentInfo.STRING},
                false);
    }

    @Override
    public String execute(User user, ProgramController programController, String[] args)
            throws IncorrectArgumentException, IOException, ClassNotFoundException {
        if (user.getLogin() != null)
            return "Вы уже авторизованы.\n" +
                    "Для смены пользователя нужно переподключение.\n";
        try {
            if (!PasswordManager.checkPasswords(args[2],
                    programController.getDataController().getDataBaseController().getUserSalt(args[1]),
                    programController.getDataController().getDataBaseController().getUserPassword(args[1])))
                throw new IncorrectArgumentException("неверный пароль пользователя");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IncorrectArgumentException("пользователь не найден.");
        }
        return "Вы были успешно авторизованы.\n";
    }
}
