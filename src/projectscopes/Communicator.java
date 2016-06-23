package projectscopes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Tomasz Najbar
 *
 * Sends and receives messages.
 */
public class Communicator {
    // Input and Output streams.
    private DataInputStream dataInputStream = null;
    private DataOutputStream dataOutputStream = null;

    /**
     * Initializes Communicator with Input stream.
     *
     * @param dataInputStream Input stream.
     */
    public Communicator(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
    }

    /**
     * Initializes Communicator with Output stream.
     *
     * @param dataOutputStream Output stream.
     */
    public Communicator(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    /**
     * Initializes Communicator with Input and Output streams.
     *
     * @param dataInputStream Input stream.
     * @param dataOutputStream Output stream.
     */
    public Communicator(DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }

    /**
     * Sends data.
     *
     * @param object Data to be sent.
     */
    public void send(Object object) throws IOException {
        byte[] bytes = Serializer.convertToBytes(object);
        dataOutputStream.writeInt(bytes.length);
        dataOutputStream.write(bytes);
    }

    /**
     * Receives data.
     *
     * @return Received Object.
     */
    public Object receive() throws IOException, ClassNotFoundException {
        byte[] bytes = new byte[dataInputStream.readInt()];
        dataInputStream.read(bytes);

        return Serializer.convertToObject(bytes);
    }
}
