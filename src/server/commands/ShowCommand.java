package server.commands;

import data_classes.City;
import server.connection_control.User;

public class ShowCommand extends Command {
    ShowCommand() {
        super("show", "", "выводит коллекцию в консоль", null, null, false);
    }

    /**
     * Print collection
     *
     * @param programController that uses for program
     * @param args              for command from console input (args[0] is program name)
     */
    @Override
    public String execute(User user, ProgramController programController, String[] args) {
        if (programController.getDataController().isMapEmpty()) {
            return "Коллекция пуста.\n";
        }
        StringBuilder data = new StringBuilder();
        int counter = 1;
        programController.getDataController().readLock();
        for (City i : programController.getDataController().getMap().values())
            data.append("Город ").append(counter++).append("\n").append(i).append("\n");
        programController.getDataController().readUnlock();
        return data.toString();
    }
}