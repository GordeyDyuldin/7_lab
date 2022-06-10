package server.commands;

import connect_utils.CommandInfo;
import exceptions.IncorrectArgumentException;
import server.connection_control.User;

import java.sql.SQLException;

public class RemoveKeyCommand extends Command {
    RemoveKeyCommand() {
        super("remove_key", "id", "удаляет элемент из коллекции с заданным ключом", null,
                new CommandInfo.ArgumentInfo[]{CommandInfo.ArgumentInfo.ID}, false);
    }

    /**
     * remove element with id from args
     * <p>Change modification time if command completes</p>
     *
     * @param programController that uses for program
     * @param args              id
     * @throws IncorrectArgumentException if id is incorrect
     */
    @Override
    public String execute(User user, ProgramController programController, String[] args)
            throws IncorrectArgumentException {
        long id = Long.parseLong(args[1]);
        if (programController.getDataController().isUniqueId(id))
            throw new IncorrectArgumentException("элемента с таким id не существует");
        try {
            if (!programController.getDataController().getDataBaseController().isOwner(user.getLogin(), id))
                return "Вы не владеете этим объектом и не можете его удалить.\n";
            programController.getDataController().removeCity(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IncorrectArgumentException("не удалось удалить элемент из базы данных");
        }
        programController.getDataController().updateModificationTime();
        return "Элемент с id " + id + " был удалён.\n";
    }
}
