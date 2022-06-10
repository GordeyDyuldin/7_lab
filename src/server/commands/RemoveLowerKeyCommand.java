package server.commands;

import connect_utils.CommandInfo;
import data_classes.City;
import exceptions.IncorrectArgumentException;
import server.connection_control.User;

import java.sql.SQLException;


public class RemoveLowerKeyCommand extends Command {
    RemoveLowerKeyCommand() {
        super("remove_lower_key", "id", "удаляет все элементы из коллекции, у которых id меньше заданного", null,
                new CommandInfo.ArgumentInfo[]{CommandInfo.ArgumentInfo.ID}, false);
    }

    /**
     * remove all elements with id that lower than id in args
     * <p>Modification time can be changed</p>
     *
     * @param programController that uses for program
     * @param args              id
     * @throws IncorrectArgumentException if id is incorrect
     */
    @Override
    public String execute(User user, ProgramController programController, String[] args)
            throws IncorrectArgumentException {
        long id = Long.parseLong(args[1]);
        boolean isMapModified = false;
        for (City city : programController.getDataController().getMap().values()) {
            if (city.getId() < id) {
                try {
                    if (!programController.getDataController().getDataBaseController()
                            .isOwner(user.getLogin(), city.getId()))
                        continue;
                    programController.getDataController().removeCity(city.getId());
                    isMapModified = true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (isMapModified) {
            programController.getDataController().updateModificationTime();
            return "Коллекция была изменена.\n";
        }
        return "Коллекция осталась без изменений.\n";
    }
}
