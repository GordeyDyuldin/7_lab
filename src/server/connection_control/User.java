package server.connection_control;

import server.Logger;
import server.commands.Command;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Class that store login, history of command and socket of user
 */
public class User {
    /**
     * Constant of max value in history
     */
    public static final int MAX_COMMANDS_IN_HISTORY = 13;

    /**
     * User's history of command
     */
    private final ArrayList<Command> history = new ArrayList<>();

    /**
     * Socket of this user
     */
    private final Socket socket;

    /**
     * Login of this user
     */
    private volatile String login;

    /**
     * Connection status
     */
    private volatile boolean isConnected;

    /**
     * Create user
     *
     * @param socket of this user
     * @param login  of this user
     */
    public User(Socket socket, String login) {
        this.socket = socket;
        this.login = login;
        isConnected = true;
    }

    /**
     * Close connection with user
     * Switch isConnected to false
     */
    public void disconnect() {
        Logger.logDisconnect(this);
        ConnectionController.disconnect(socket);
        isConnected = false;
    }

    /**
     * Set login of user
     *
     * @param login that need to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Add new command to history
     *
     * @param command that need to add
     */
    public void addCommandToHistory(Command command) {
        if (history.size() == MAX_COMMANDS_IN_HISTORY) {
            history.remove(0);
        }
        history.add(command);
    }

    /**
     * Return history of command
     *
     * @return array list of commands
     */
    public ArrayList<Command> getHistory() {
        return history;
    }

    /**
     * Get status of this user
     * After creation is true but after using disconnect() set to false
     *
     * @return status of user's connection
     */
    public boolean isDisconnected() {
        return !isConnected;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getLogin() {
        return login;
    }

    /**
     * Get IP-address of user
     *
     * @return user's address
     */
    public String getAddress() {
        return socket.getInetAddress().getHostAddress();
    }
}
