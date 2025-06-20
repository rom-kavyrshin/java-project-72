package hexlet.code.util;

public class NamedRoutes {

    public static String rootPath() {
        return "/";
    }

    public static String urlsPath() {
        return "/urls";
    }

    public static String urlDetailPath(long urlId) {
        return urlDetailPath(String.valueOf(urlId));
    }

    public static String urlDetailPath(String urlId) {
        return urlsPath() + "/" + urlId;
    }

    public static String urlCheckPath(long urlId) {
        return urlCheckPath(String.valueOf(urlId));
    }

    public static String urlCheckPath(String urlId) {
        return urlsPath() + "/" + urlId + "/checks";
    }
}
