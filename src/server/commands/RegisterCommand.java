package server.commands;

import connect_utils.CommandInfo;
import exceptions.IncorrectArgumentException;
import server.connection_control.User;
import server.data_control.PasswordManager;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterCommand extends Command {
    RegisterCommand() {
        super("register", "login password", "регистрирует нового пользователя в системе",
                null, new CommandInfo.ArgumentInfo[]{CommandInfo.ArgumentInfo.STRING,
                        CommandInfo.ArgumentInfo.STRING}, false);
    }

    @Override
    public String execute(User user, ProgramController programController, String[] args)
            throws IncorrectArgumentException, IOException, ClassNotFoundException {
        String password;
        String salt = PasswordManager.generateSalt();
        try {
            password = PasswordManager.createHash(args[2] + salt);
            programController.getDataController().createUser(args[1], password, salt);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IncorrectArgumentException("Пользователь с таким логином уже существует.");
        }
        if (user.getLogin() != null)
            return "Был зарегистрирован новый пользователь.\n" +
                    "Ваш логин остался без изменения. Для смены пользователя переподключитесь.\n";
        return "Вы были успешно зарегистрированы и авторизованы.\n";
    }
}
