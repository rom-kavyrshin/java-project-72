package hexlet.code.util;

public class Util {

    public static String getEnv(String key, String defaultValue) {
        return System.getenv().getOrDefault(key, defaultValue);
    }
}
