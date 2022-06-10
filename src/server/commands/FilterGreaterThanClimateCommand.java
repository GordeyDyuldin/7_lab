package server.commands;

import connect_utils.CommandInfo;
import data_classes.City;
import data_classes.Climate;
import exceptions.IncorrectArgumentException;
import server.connection_control.User;

import java.io.IOException;

public class FilterGreaterThanClimateCommand extends Command {
    FilterGreaterThanClimateCommand() {
        super("filter_greater_than_climate", "climate",
                "выводит элементы, у которых значение поля climate больше заданного", null,
                new CommandInfo.ArgumentInfo[]{CommandInfo.ArgumentInfo.CLIMATE}, false);
    }

    /**
     * print elements that have got climate that greater than climate from args
     *
     * @param programController that uses for program
     * @param args              climate
     * @throws IncorrectArgumentException if climate is incorrect
     */
    @Override
    public String execute(User user, ProgramController programController, String[] args)
            throws IncorrectArgumentException, IOException {
        Climate climate = null;
        for (Climate i : Climate.values()) {
            if (i.toString().equals(args[1].toUpperCase())) {
                climate = i;
                break;
            }
        }
        boolean isExist = false;
        programController.getDataController().readLock();
        if (climate != null) {
            for (City i : programController.getDataController().getMap().values()) {
                if (i.getClimate() == null)
                    continue;
                if (i.getClimate().ordinal() > climate.ordinal()) {
                    isExist = true;
                    break;
                }
            }
        } else
            isExist = true;
        if (!isExist) {
            return "Таких элементов не существует в коллекции.\n";
        }
        StringBuilder data = new StringBuilder();
        Climate finalClimate = climate;
        programController.getDataController().getMap().values().forEach(city -> {
            if (finalClimate == null && city.getClimate() != null)
                data.append(city).append("\n");
            else if (city.getClimate() == null && finalClimate == null) ;
            else if (city.getClimate().ordinal() > finalClimate.ordinal())
                data.append(city).append("\n");
        });
        programController.getDataController().readUnlock();
        return data.toString();
    }
}
