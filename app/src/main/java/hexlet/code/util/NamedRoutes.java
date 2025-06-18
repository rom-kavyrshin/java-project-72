package hexlet.code.util;

public class NamedRoutes {

    public static String rootPath() {
        return "/";
    }

    public static String urlsPath() {
        return "/urls";
    }

    public static String urlPath(long urlId) {
        return urlPath(String.valueOf(urlId));
    }

    public static String urlPath(String urlId) {
        return urlsPath() + "/" + urlId;
    }

    public static String urlCheckPath(long urlId) {
        return urlCheckPath(String.valueOf(urlId));
    }

    public static String urlCheckPath(String urlId) {
        return urlsPath() + "/" + urlId + "/checks";
    }
}
