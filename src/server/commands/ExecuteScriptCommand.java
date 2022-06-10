package server.commands;

import connect_utils.*;
import exceptions.IncorrectArgumentException;
import connect_utils.CommandInfo;
import server.connection_control.User;

import java.io.IOException;

public class ExecuteScriptCommand extends Command {
    /**
     * count of usage execute_script
     */
    private int recursionCounter = 0;

    /**
     * stop value for recursion
     */
    public final static int RECURSION_INTERRUPT = 10;

    ExecuteScriptCommand() {
        super("execute_script", "file_name", "исполняет скрипт из указанного файла", CommandInfo.SendInfo.COMMANDS,
                new CommandInfo.ArgumentInfo[]{CommandInfo.ArgumentInfo.STRING}, false);
    }

    /**
     * Execute script file from client
     *
     * @param user              that execute this command
     * @param programController that execute this command
     * @param args              of command
     * @return null
     * @throws IOException            if connection was closed
     * @throws ClassNotFoundException if server receive something that not expected
     */
    @Override
    public String execute(User user, ProgramController programController, String[] args)
            throws IOException, ClassNotFoundException {
        programController.getConnectionController().getRequestController().sendOK(user.getSocket());
        DataTransferObject dataTransferObject = programController.getConnectionController().getRequestController()
                .receiveRequest(user.getSocket());
        Command command;
        String[] cArgs;
        String reply = null;
        while (!dataTransferObject.getCode().equals(DataTransferObject.Code.OK)) {
            cArgs = dataTransferObject.getMsg().split(" ");
            command = programController.searchCommand(cArgs[0]);
            if (command != null) {
                if (command.getName().equals("execute_script"))
                    recursionCounter++;
                if (recursionCounter >= RECURSION_INTERRUPT) {
                    programController.getConnectionController().getRequestController()
                            .sendError(user.getSocket(), "Глубина рекурсии слишком большая " +
                                    "(рекурсия может быть глубиной до "
                                    + RECURSION_INTERRUPT + ".\nВыход из рекурсии..");
                }
                try {
                    reply = programController.invoke(user, command, cArgs);
                } catch (IncorrectArgumentException e) {
                    programController.getConnectionController().getRequestController()
                            .sendError(user.getSocket(), "получен некорректный аргумент - " + e.getMessage());
                } catch (IOException e) {
                    user.disconnect();
                    return null;
                } catch (ClassNotFoundException ignored) {

                }
                if (reply != null)
                    programController.getConnectionController().getRequestController()
                            .sendReply(user.getSocket(), reply);
            }
            dataTransferObject = programController.getConnectionController().getRequestController()
                    .receiveRequest(user.getSocket());
        }
        recursionCounter = 0;
        programController.getConnectionController().getRequestController()
                .sendOK(user.getSocket());
        return null;
    }


}
