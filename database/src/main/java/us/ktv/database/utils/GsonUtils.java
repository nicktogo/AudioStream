package us.ktv.database.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by nick on 15-10-6.
 */
public class GsonUtils {

    private static Gson gson = new Gson();

    public static String ObjectToJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T JsonToObject(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T JsonToObject(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

}
