package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;

public class JSONUtils {
    public static String toJson(Object o) {
        return new JSONObject(o).toString();
    }

    public static <T extends Object> T fromJson(Object o, Class<T> classType) {
        T parsedObj = null;

        try {
            parsedObj = new ObjectMapper().readValue(o.toString(), classType);
        }
        catch (IOException exception) {
            System.out.println("Error parsing JSON: Class["+classType.toString()+"]");
            exception.printStackTrace();
        }
        return parsedObj;
    }
}
