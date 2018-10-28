package tetris.externaltools;
 
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import tetris.code.highScoreObject;
 
/**
 * This class is a utility class for performing the serialization and
 * deserialization operations provided the required information.
 *
 * @author hiteshgarg
 */
public class SerializationUtil {
 
    /**
     * deserialize to Object from given file. We use the general Object so as
     * that it can work for any Java Class.
     */
    public static highScoreObject deserialize(String fileName) throws IOException,
        	ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        highScoreObject obj = (highScoreObject) ois.readObject();
        ois.close();
        return obj;
	}
 
    /**
     * serialize the given object and save it to given file
     */
    public static void serialize(highScoreObject obj, String fileName)
            throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();
    }
}