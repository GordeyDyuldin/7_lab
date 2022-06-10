package server.connection_control;

//import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
//import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import connect_utils.CommandInfo;
import connect_utils.DataTransferObject;
import data_classes.City;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Control all requests between server and clients
 */
public class RequestController {

    private final ConnectionController connectionController;

    RequestController(ConnectionController connectionController) {
        this.connectionController = connectionController;
    }

    /**
     * Send OK request to user
     *
     * @throws IOException if server couldn't send this request
     */
    public void sendOK(Socket socket) throws IOException {
        connectionController.sendObject(socket, new DataTransferObject(DataTransferObject.Code.OK, ""));
    }

    /**
     * Send REPLY request to user
     *
     * @param msg that user can see as result of command execution
     * @throws IOException if server couldn't send this request
     */
    public void sendReply(Socket socket, String msg) throws IOException {
        connectionController.sendObject(socket, new DataTransferObject(DataTransferObject.Code.REPLY, msg));
    }

    /**
     * Get OK-reply
     *
     * @param socket of client
     * @throws IOException if connection was closed
     */
    public void receiveOK(Socket socket) throws IOException {
        try {
            receiveRequest(socket);
        } catch (ClassNotFoundException ignored) {

        }
    }

    /**
     * Send ERROR request
     *
     * @param msg that user can see as explanation of error
     * @throws IOException if server couldn't send this request
     */
    public void sendError(Socket socket, String msg) throws IOException {
        connectionController.sendObject(socket, new DataTransferObject(DataTransferObject.Code.ERROR, msg + "\n"));
    }

    /**
     * Send list of command that user can use on server
     *
     * @param socket of user that need to send it
     * @param list   of commands
     * @throws IOException if connection is closed
     */
    public void sendCommandList(Socket socket, ArrayList<CommandInfo> list) throws IOException {
        DataTransferObject dto = new DataTransferObject(DataTransferObject.Code.NOT_REQUEST,
                connectionController.convertObjectToBytes(list), DataTransferObject.DataType.COMMANDS_ARRAY);
        connectionController.sendObject(socket, dto);
    }

    /**
     * Receive request from user
     *
     * @return request
     * @throws IOException            if server couldn't receive this request
     * @throws ClassNotFoundException if server received not expected class
     */
    public DataTransferObject receiveRequest(Socket socket) throws IOException, ClassNotFoundException {
        return connectionController.receiveObject(socket);
    }

    /**
     * Get city from client
     *
     * @param socket of client
     * @return city that was received
     * @throws IOException            if connection was closed
     * @throws ClassNotFoundException if receiving information is not city
     */
    public City receiveCity(Socket socket) throws IOException, ClassNotFoundException {
        DataTransferObject dto = connectionController.receiveObject(socket);
        //ByteInputStream byteStream = new ByteInputStream();
        //byteStream.setBuf(dto.getDataBytes());
        //ObjectInputStream objIn = new ObjectInputStream(byteStream);
        //return (City) objIn.readObject();
        return null;
    }
}
