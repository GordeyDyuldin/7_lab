package server.commands;

import connect_utils.CommandInfo;
import data_classes.Coordinates;
import data_classes.City;

/**
 * abstract class for all commands
 * <p>Use executable interface</p>
 */
public abstract class Command implements Executable {
    /**
     * name of this command for usage
     */
    private final String name;

    /**
     * description of this command (uses for help command)
     */
    private final String description;

    /**
     * signature of this command (uses for help command)
     */
    private final String signature;

    /**
     * Information that client need to send to server when command is executing
     */
    private final CommandInfo.SendInfo sendInfo;

    /**
     * Arguments that client need to check before he sends ones to server
     */
    private final CommandInfo.ArgumentInfo[] argInfo;

    /**
     * true if it's command that can use only from server console without client
     */
    private final boolean isServerCommand;

    /**
     * Create new command that can execute on server
     *
     * @param name            of this command
     * @param signature       that show arguments of this command (for help)
     * @param description     of this command (for help)
     * @param sendInfo        of this command (for client)
     * @param argInfo         of this command (for client)
     * @param isServerCommand if true client can't use it
     */
    public Command(final String name, final String signature, final String description, CommandInfo.SendInfo sendInfo,
            CommandInfo.ArgumentInfo[] argInfo, boolean isServerCommand) {
        this.name = name;
        this.signature = signature;
        this.description = description;
        this.sendInfo = sendInfo;
        this.argInfo = argInfo;
        this.isServerCommand = isServerCommand;
    }

    /**
     * Delete null values in newCity.
     * <p>newCity will be <b>changed</b> after this method</p>
     *
     * @param oldCity where we take data
     * @param newCity where we put data
     */
    protected void deleteNullValues(final City oldCity, final City newCity) {
        if (newCity.getName() == null)
            newCity.setName(oldCity.getName());
        if (newCity.getCoordinates().getX() == Coordinates.X_INIT_VALUE)
            newCity.getCoordinates().setX(oldCity.getCoordinates().getX());
        if (newCity.getCoordinates().getY() == null)
            newCity.getCoordinates().setY(oldCity.getCoordinates().getY());
        if (newCity.getEstablishmentDate() == null)
            newCity.setEstablishmentDate(oldCity.getEstablishmentDate());
        if (newCity.getArea() == 0)
            newCity.setArea(oldCity.getArea());
        if (newCity.getPopulation() == 0)
            newCity.setPopulation(oldCity.getPopulation());
        if (newCity.getMetersAboveSeaLevel() == null)
            newCity.setMetersAboveSeaLevel(oldCity.getMetersAboveSeaLevel());
        if (newCity.getClimate() == null)
            newCity.setClimate(oldCity.getClimate());
        if (newCity.getGovernment() == null)
            newCity.setGovernment(oldCity.getGovernment());
        if (newCity.getGovernor().getAge() == null)
            newCity.getGovernor().setAge(oldCity.getGovernor().getAge());
        if (newCity.getGovernor().getBirthday() == null)
            newCity.getGovernor().setBirthday(oldCity.getGovernor().getBirthday());
    }

    public String getName() {
        return name;
    }

    public String getSignature() {
        return signature;
    }

    public String getDescription() {
        return description;
    }

    public CommandInfo.SendInfo getSendInfo() {
        return sendInfo;
    }

    public CommandInfo.ArgumentInfo[] getArgInfo() {
        return argInfo;
    }

    public boolean isServerCommand() {
        return isServerCommand;
    }
}
