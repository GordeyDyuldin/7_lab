package server.commands;

import server.connection_control.User;

import java.io.IOException;
import java.util.ArrayList;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "", "вызывает описание всех команд", null, null, false);
    }

    /**
     * print name, signature, description for all command
     *
     * @param programController that uses for program
     * @param args              for command from console input (args[0] is program name)
     */
    @Override
    public String execute(User user, ProgramController programController, String[] args) throws IOException {
        StringBuilder data = new StringBuilder();
        ArrayList<Command> commands;
        if (user.getLogin() != null)
            commands = programController.getAllCommands();
        else
            commands = programController.getAuthCommands();
        commands.forEach(command -> {
            if (!command.isServerCommand())
                data.append(String.format("%-50s - %-1s %n", command.getName() + " " + command.getSignature(),
                        command.getDescription()));
        });
        return data.toString();
    }
}
