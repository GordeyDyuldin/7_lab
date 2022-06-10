package client.connection_control;

import connect_utils.CommandInfo;
import connect_utils.DataTransferObject;
import data_classes.City;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/**
 * Class uses for control all requests
 */
public class RequestController {
    private final ConnectionController connectionController;

    RequestController(ConnectionController connectionController) {
        this.connectionController = connectionController;
    }

    /**
     * Convert receiving object to Request
     *
     * @return Request from server
     * @throws IOException            if Request couldn't receive
     * @throws ClassNotFoundException if object couldn't deserialize
     */
    public DataTransferObject receiveRequest() throws IOException, ClassNotFoundException {
        return connectionController.processConnection();
    }

    /**
     * Send request to server
     *
     * @param channel that connect with server
     * @param dataTransferObject that need to send
     * @throws IOException if request couldn't send
     */
    public void sendRequest(SocketChannel channel, DataTransferObject dataTransferObject) throws IOException {
        connectionController.sendObject(channel, dataTransferObject);
    }

    public void sendOK(SocketChannel channel) throws IOException {
        connectionController.sendObject(channel, new DataTransferObject(DataTransferObject.Code.OK, ""));
    }
    /**
     * Send city object to server
     *
     * @param channel that connect with server
     * @param city    that need to send
     * @throws IOException if City object couldn't send
     */
    public void sendCity(SocketChannel channel, City city) throws IOException {
        DataTransferObject dto = new DataTransferObject(
                DataTransferObject.Code.NOT_REQUEST, connectionController.convertObjectToBytes(city),
                DataTransferObject.DataType.CITY);
        connectionController.sendObject(channel, dto);
    }

    public ArrayList<CommandInfo> getCommandInfos() throws IOException, ClassNotFoundException {
        DataTransferObject dto = connectionController.processConnection();
        ByteArrayInputStream byteStream = new ByteArrayInputStream(dto.getDataBytes());
        ObjectInputStream objIn = new ObjectInputStream(byteStream);
        return (ArrayList<CommandInfo>) objIn.readObject();
    }
}
