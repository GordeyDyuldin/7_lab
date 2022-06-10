package server.commands;

import exceptions.IncorrectArgumentException;
import server.connection_control.User;

import java.sql.SQLException;

public class ClearCommand extends Command {
    ClearCommand() {
        super("clear", "", "очищает элементы коллекции", null, null, false);
    }

    /**
     * Delete all cities that user is owned
     *
     * @param user              that execute this command
     * @param programController that execute this command
     * @param args              of command
     * @return reply
     * @throws IncorrectArgumentException if database return error
     */
    @Override
    public String execute(User user, ProgramController programController, String[] args)
            throws IncorrectArgumentException {
        try {
            programController.getDataController().clearMap(user.getLogin());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IncorrectArgumentException("не удалось удалить данные из базы данных");
        }
        programController.getDataController().updateModificationTime();
        return "Коллекция успешно очищена от ваших объектов.\n";
    }
}
