package server.commands;

import exceptions.ConfigFileNotFoundException;
import connect_utils.CommandInfo;
import server.Logger;
import server.connection_control.ConnectionController;
import connect_utils.*;
import server.connection_control.User;
import server.data_control.DataController;
import exceptions.IncorrectArgumentException;
import exceptions.MissingArgumentException;
import exceptions.UnknownCommandException;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.logging.Level;


/**
 * controls execution of all commands
 */
public class ProgramController {

    /**
     * that controls data for program
     */
    private final DataController dataController;

    /**
     * that controls connection with user
     */
    private final ConnectionController connectionController;

    /**
     * collection of all commands that user can use
     */
    private final ArrayList<Command> allCommands = new ArrayList<>(16);

    /**
     * collection of data about all commands that will send to user
     */
    private final ArrayList<CommandInfo> allCommandsInfo = new ArrayList<>(16);

    /**
     *
     */
    private final ArrayList<Command> authCommands = new ArrayList<>(3);

    private final ExecutorService listeners = Executors.newCachedThreadPool();
    private final ExecutorService executors = Executors.newFixedThreadPool(5);
    private final ForkJoinPool senders = new ForkJoinPool(3);

    /**
     * Create program working class
     */
    public ProgramController() throws SQLException, MissingArgumentException, ConfigFileNotFoundException {
        this.dataController = new DataController();
        connectionController = new ConnectionController();
        commandInit();
    }

    /**
     * Start work of program: turn on connection controller and receive connection
     */
    public void start() {
        try {
            connectionController.start();
        } catch (IOException e) {
            Logger.getLogger().log(Level.WARNING, "Не удалось развернуть сервер. " +
                    "Попробуйте развернуть его на другом порте.");
            return;
        }
        listeners.execute(this::processClient);
    }

    /**
     * Wait creating connection from user
     */
    private void processClient() {
        Socket socket;
        try {
            socket = connectionController.connect();
            connectionController.getRequestController().sendCommandList(socket, allCommandsInfo);
        } catch (IOException e) {
            Logger.getLogger().log(Level.WARNING, "Ошибка попытки соединения с клиентом.");
            return;
        } finally {
            listeners.execute(this::processClient);
        }
        User user = new User(socket, null);
        while (user.getLogin() == null) {
            user.setLogin(clientAuth(user));
            if (user.isDisconnected())
                return;
        }
        listenRequests(user);
    }

    /**
     * Initialization commands to allCommands that can be used by user
     */
    private void commandInit() {
        allCommands.add(new HelpCommand());
        allCommands.add(new RegisterCommand());
        allCommands.add(new LoginCommand());
        allCommands.add(new ExitCommand());

        allCommands.add(new InfoCommand());
        allCommands.add(new ShowCommand());
        allCommands.add(new InsertCommand());
        allCommands.add(new UpdateCommand());
        allCommands.add(new RemoveKeyCommand());
        allCommands.add(new ClearCommand());
        allCommands.add(new ExecuteScriptCommand());
        allCommands.add(new HistoryCommand());
        allCommands.add(new ReplaceIfGreaterCommand());
        allCommands.add(new RemoveLowerKeyCommand());
        allCommands.add(new FilterGreaterThanClimateCommand());
        allCommands.add(new PrintAscendingCommand());
        allCommands.add(new PrintFieldAscendingGovernment());
        allCommands.forEach(command -> {
            if (!command.isServerCommand())
                allCommandsInfo.add(new CommandInfo(command.getName(), command.getSendInfo(), command.getArgInfo()));
        });
        authCommands.add(allCommands.get(0));
        authCommands.add(allCommands.get(1));
        authCommands.add(allCommands.get(2));
        authCommands.add(allCommands.get(3));
    }

    private String clientAuth(User user) {
        DataTransferObject dataTransferObject;
        try {
            dataTransferObject = connectionController.getRequestController().receiveRequest(user.getSocket());
        } catch (IOException e) {
            e.printStackTrace();
            user.disconnect();
            return null;
        } catch (ClassNotFoundException e) {
            Logger.getLogger().log(Level.WARNING, "Получен некорректный запрос от клиента.");
            return null;
        }
        String[] args = dataTransferObject.getMsg().split(" ");
        if (authCommands.contains(searchCommand(args[0]))) {
            Future<String> result =
                    executors.submit(() -> {
                        try {
                            return invoke(user, searchCommand(args[0]), args);
                        } catch (IOException e) {
                            e.printStackTrace();
                            user.disconnect();
                        } catch (IncorrectArgumentException e) {
                            Logger.getLogger().log(Level.INFO, "Ошибка авторизации: " + e.getMessage());
                            senders.execute(() -> {
                                try {
                                    connectionController.getRequestController()
                                            .sendError(user.getSocket(), e.getMessage());
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                    user.disconnect();
                                }
                            });
                        } catch (ClassNotFoundException e) {
                            Logger.getLogger().log(Level.WARNING, "Ошибка получения запроса от клиента");
                        }
                        return null;
                    });
            try {
                return senders.submit(() -> {
                    try {
                        connectionController.getRequestController().sendReply(user.getSocket(), result.get());
                        return args[1];
                    } catch (IOException e) {
                        e.printStackTrace();
                        user.disconnect();
                    } catch (InterruptedException | ExecutionException ignored) {

                    }
                    return null;
                }).get();
            } catch (InterruptedException | ExecutionException ignored) {

            }
        } else {
            senders.execute(() -> {
                try {
                    connectionController.getRequestController().sendError(user.getSocket(),
                            "Доступ запрещен " +
                                    "неавторизованным пользователям.\nИспользуйте команды login или register "
                                    + "для авторизации или регистрации.\n" +
                                    "Пример использования: login sadness 1234");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    user.disconnect();
                }
            });
        }
        return null;
    }

    /**
     * Use when connection with user exists. Listen request from user and execute command from one.
     */
    private void listenRequests(User user) {
        if (user.isDisconnected())
            return;
        String[] input;
        DataTransferObject dataTransferObject;
        Command command;
        try {
            dataTransferObject = connectionController.getRequestController().receiveRequest(user.getSocket());
            if (!dataTransferObject.getCode().equals(DataTransferObject.Code.COMMAND)) {
                Logger.getLogger().log(Level.WARNING, "Получен некорректный запрос от клиента.");
                listeners.execute(() -> listenRequests(user));
            }
            input = dataTransferObject.getMsg().split(" ");
        } catch (IOException e) {
            e.printStackTrace();
            Logger.getLogger().log(Level.WARNING, "Ошибка получения запроса");
            user.disconnect();
            return;
        } catch (ClassNotFoundException e) {
            Logger.getLogger().log(Level.WARNING, "Получен некорректный запрос от клиента.");
            listeners.execute(() -> listenRequests(user));
            return;
        }
        command = searchCommand(input[0].toLowerCase());
        Future<String> futureReply = executors.submit(() -> {
            try {
                try {
                    return invoke(user, command, input);
                } catch (IncorrectArgumentException e) {
                    Logger.getLogger().log(Level.WARNING, "Некорректный аргумент: " + e.getMessage());
                    senders.execute(() -> {
                        try {
                            connectionController.getRequestController()
                                    .sendError(user.getSocket(),
                                            "получен некорректный аргумент команды - " + e.getMessage());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            user.disconnect();
                        }
                    });

                } catch (UnknownCommandException e) {
                    Logger.getLogger().log(Level.WARNING, "Получена команда, неизвестная серверу.");
                    senders.execute(() -> {
                        try {
                            connectionController.getRequestController()
                                    .sendError(user.getSocket(), "получена неизвестная серверу команда");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            user.disconnect();
                        }
                    });

                } catch (ClassNotFoundException e) {
                    Logger.getLogger().log(Level.WARNING, "Получены неопознанные данные от клиента");
                    senders.execute(() -> {
                        try {
                            connectionController.getRequestController()
                                    .sendError(user.getSocket(), "получены неопознанные данные от клиента");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            user.disconnect();
                        }
                    });

                }
            } catch (IOException e) {
                e.printStackTrace();
                user.disconnect();
            }
            return null;
        });

        senders.execute(() -> {
            String reply = null;
            try {
                reply = futureReply.get();
            } catch (InterruptedException | ExecutionException ignored) {

            }
            if (reply != null) {
                try {
                    connectionController.getRequestController().sendReply(user.getSocket(), reply);
                    Logger.getLogger().log(Level.INFO, "Отправлен ответ клиенту " + user.getLogin() + ".");
                } catch (IOException e) {
                    e.printStackTrace();
                    user.disconnect();
                }
            }
            listeners.execute(() -> listenRequests(user));
        });
    }

    /**
     * Execute command and add it in history
     *
     * @param command that need to invoke
     * @param args    for this command
     * @throws IncorrectArgumentException if requiring args is incorrect
     */
    protected String invoke(User user, final Command command, final String[] args)
            throws IncorrectArgumentException, IOException, ClassNotFoundException {
        Logger.getLogger().log(Level.INFO, "Получена команда " + command.getName() + " от клиента " +
                (user.getLogin() == null ? user.getAddress() : user.getLogin()));
        user.addCommandToHistory(command);
        return command.execute(user, this, args);
    }

    /**
     * Parse string to command
     *
     * @param name of command
     * @return command
     * @throws UnknownCommandException if name of command doesn't equal with name in command's constructor
     */
    protected Command searchCommand(final String name) throws UnknownCommandException {
        for (Command i : allCommands) {
            if (i.getName().equals(name))
                return i;
        }
        throw new UnknownCommandException();
    }

    public void stop() {
        listeners.shutdownNow();
        executors.shutdown();
        boolean done = false;
        try {
            done = executors.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {

        }
        if (done)
            Logger.getLogger().log(Level.INFO, "Все полученные команды были исполнены.");
        else
            Logger.getLogger().log(Level.INFO, "Не все команды были исполнены.");
        senders.shutdown();
    }

    public DataController getDataController() {
        return dataController;
    }

    public ConnectionController getConnectionController() {
        return connectionController;
    }

    public ArrayList<Command> getAllCommands() {
        return allCommands;
    }

    public ArrayList<Command> getAuthCommands() {
        return authCommands;
    }
}
