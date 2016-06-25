package projectscopes;

import java.io.*;

/**
 * @author Tomasz Najbar
 *
 * Serializes data to bytes array and deserializes to Object.
 */
public class Serializer {
    /**
     * Serializes bytes array from Object.
     *
     * @param object Object to be serialized.
     * @return Bytes representation of the object.
     */
    public static byte[] convertToBytes(Object object) {
        byte[] bytes = null;

        try {
            ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream();
            new ObjectOutputStream(bytesOutputStream).writeObject(object);
            bytes = bytesOutputStream.toByteArray();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return bytes;
    }

    /**
     * De-serializes bytes array to Object.
     *
     * @param bytes Bytes to be de-serialized.
     * @return Object representation of bytes array.
     */
    public static Object convertToObject(byte[] bytes) {
        Object object = null;

        try {
            object = new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }

        return object;
    }
}
