package server.commands;

import data_classes.City;
import exceptions.IncorrectArgumentException;
import connect_utils.CommandInfo;
import server.connection_control.User;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateCommand extends Command {
    UpdateCommand() {
        super("update", "id {element}", "обновляет значение элемента коллекции с определенным id",
                CommandInfo.SendInfo.CITY_UPDATE,
                new CommandInfo.ArgumentInfo[]{CommandInfo.ArgumentInfo.ID}, false);
    }

    /**
     * update element with id from args by new from console input
     *
     * @param programController that uses for program
     * @param args              id
     * @throws IncorrectArgumentException if id is incorrect
     */
    @Override
    public String execute(User user, ProgramController programController, String[] args)
            throws IncorrectArgumentException, IOException, ClassNotFoundException {
        Long id = Long.parseLong(args[1]);
        if (programController.getDataController().isUniqueId(id))
            throw new IncorrectArgumentException("элемента с таким id не существует");
        try {
            if (!programController.getDataController().getDataBaseController().isOwner(user.getLogin(), id))
                throw new IncorrectArgumentException("вы не владеете этим объектом. Вы не можете его изменять.");

            programController.getConnectionController().getRequestController()
                    .sendOK(user.getSocket());
            City city = programController.getConnectionController().getRequestController()
                    .receiveCity(user.getSocket());
            if (city == null)
                throw new IncorrectArgumentException("город не был создан");
            city.setId(id);
            deleteNullValues(programController.getDataController().getMap().get(id), city);
            programController.getDataController().updateCity(city);
            programController.getDataController().updateModificationTime();
            return "Элемент с id " + id + " был обновлён.\n";
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IncorrectArgumentException("не удалось обновить элемент в базе данных");
        }
    }
}
