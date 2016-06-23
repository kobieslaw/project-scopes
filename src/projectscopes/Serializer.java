package projectscopes;

import java.io.*;

/**
 *
 * @author Tomasz Najbar
 *
 * Serializes data to bytes array and deserializes to Object.
 */
public class Serializer {
    /**
     * Serialize bytes array from Object.
     *
     * @param object Object to be serialized.
     * @return Bytes representation of the object.
     * @throws IOException
     */
    public static byte[] convertToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream();
             ObjectOutput objectOutput = new ObjectOutputStream(bytesOutputStream)) {
            objectOutput.writeObject(object);
            return bytesOutputStream.toByteArray();
        }
    }

    /**
     * Deserialize bytes array to Object.
     *
     * @param bytes Bytes to be deserialized.
     * @return Object representation of bytes array.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object convertToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bytesInputStream = new ByteArrayInputStream(bytes);
             ObjectInput objectInput = new ObjectInputStream(bytesInputStream)) {
            return objectInput.readObject();
        }
    }
}
