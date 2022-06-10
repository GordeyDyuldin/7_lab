package connect_utils;

import java.io.Serializable;

/**
 * Object that server always send/receive to/from client
 */
public class DataTransferObject implements Serializable {

    /**
     * code of this request
     */
    private final Code code;

    /**
     *
     */
    private final DataType dataType;
    /**
     * byte array of msg
     */
    private final byte[] dataBytes;

    /**
     * Create data object for sending
     *
     * @param code of this request
     * @param msg         of this request
     */
    public DataTransferObject(final Code code, final String msg) {
        this.code = code;
        dataBytes = msg.getBytes();
        dataType = DataType.MESSAGE;
    }

    /**
     * Create data object for sending
     *
     * @param code of this request
     * @param bytes       of message for this request
     */
    public DataTransferObject(final Code code, final byte[] bytes, DataType dataType) {
        this.code = code;
        dataBytes = bytes;
        this.dataType = dataType;
    }

    public String getMsg() {
        return new String(dataBytes);
    }

    public Code getCode() {
        return code;
    }

    public byte[] getDataBytes() {
        return dataBytes;
    }

    public DataType getDataType() {
        return dataType;
    }

    public enum DataType {
        MESSAGE,
        CITY,
        CITIES_ARRAY,
        COMMANDS_ARRAY
    }

    /**
     * Some codes for init request
     * REPLY - result of command that prints on client's screen
     * COMMAND - command with arguments that need to execute on server
     * ERROR - explanation of error with arguments or execution
     * NEXT_REQUEST_CITY - next object for receiving is City
     * OK - all arguments that need to check on server is ok OR server is ready to continue processing of command
     * PART_OF_DATE - if it not all data what sender was sent
     */
    public enum Code {
        REPLY,
        COMMAND,
        ERROR,
        NEXT_REQUEST_CITY,
        OK,
        PART_OF_DATE,
        NOT_REQUEST
    }
}
