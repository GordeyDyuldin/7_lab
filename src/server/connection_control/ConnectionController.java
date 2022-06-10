package server.connection_control;

import connect_utils.DataTransferObject;
import exceptions.ConfigFileNotFoundException;
import exceptions.MissingArgumentException;
import server.Logger;
import server.connection_control.RequestController;
import server.data_control.FilesController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;

/**
 * controls connections with users
 */
public class ConnectionController {

    /**
     * socket's factory
     */
    private ServerSocket serverSocket;

    /**
     * current port where server is located
     */
    private final int port;

    /**
     * Controller for all requests
     */
    private final RequestController requestController = new RequestController(this);

    /**
     * Create new connection controller for connecting with users.
     * Reading config file with name "config.excalibbur" with connection's data
     * File need to have "port: *digits*"
     *
     * @throws MissingArgumentException if port couldn't find in config file
     */
    public ConnectionController()
            throws ConfigFileNotFoundException, MissingArgumentException {
        port = FilesController.readConfigPort();
    }

    /**
     * Start working of server
     *
     * @throws IOException if port is using now
     */
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        Logger.getLogger().log(Level.INFO, "Сервер запущен на порте " + port + " и готов принимать клиентов.");
    }

    /**
     * Create new connection with user
     *
     * @throws IOException if connection was failed
     */
    public Socket connect() throws IOException {
        Socket socket = serverSocket.accept();
        Logger.getLogger().log(Level.INFO, "Принято подключение от клиента "
                + socket.getInetAddress() + ":" + socket.getLocalPort() + ".");
        return socket;
    }

    /**
     * Receive some data from client
     *
     * @return object (can be City or Request)
     * @throws IOException            if receiving is failed
     * @throws ClassNotFoundException if object can't be serialized
     */
    protected DataTransferObject receiveObject(Socket socket) throws IOException, ClassNotFoundException {
        return (DataTransferObject) new ObjectInputStream(socket.getInputStream()).readObject();
    }

    /**
     * Send data to client
     *
     * @throws IOException if sending is failed
     */
    protected void sendObject(Socket socket, DataTransferObject dataTransferObject) throws IOException {
        final int byteSize = 500;
        byte[] bytesDto = convertObjectToBytes(dataTransferObject);
        if (bytesDto.length > byteSize) {
            int parts = dataTransferObject.getDataBytes().length/byteSize;
            for(int i=0;i<parts;i++) {
                socket.getOutputStream().write(convertObjectToBytes(
                        new DataTransferObject(DataTransferObject.Code.PART_OF_DATE,
                                Arrays.copyOfRange(dataTransferObject.getDataBytes(),
                                        i*byteSize, (i+1)*byteSize),
                                dataTransferObject.getDataType())
                        ));
                requestController.receiveOK(socket);
            }
            if (bytesDto.length % byteSize != 0) {
                socket.getOutputStream().write(convertObjectToBytes(
                        new DataTransferObject(DataTransferObject.Code.PART_OF_DATE,
                                Arrays.copyOfRange(dataTransferObject.getDataBytes(),
                                        parts*byteSize, dataTransferObject.getDataBytes().length),
                                dataTransferObject.getDataType())
                ));
                requestController.receiveOK(socket);
            }
            socket.getOutputStream().write(convertObjectToBytes(
                    new DataTransferObject(dataTransferObject.getCode(), null, dataTransferObject.getDataType())
            ));
        }
        else {
            socket.getOutputStream().write(bytesDto);
        }
    }

    protected byte[] convertObjectToBytes(Object object) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteStream);
        objOut.writeObject(object);
        objOut.flush();
        return byteStream.toByteArray();
    }
    /**
     * Close connection with client
     */
    public static void disconnect(Socket socket) {
        try {
            if (socket != null)
                socket.close();
        } catch (IOException ignored) {

        }
    }

    public RequestController getRequestController() {
        return requestController;
    }
}