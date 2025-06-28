package hexlet.code.util;

public final class NamedRoutes {

    private NamedRoutes() {
    }

    public static String rootPath() {
        return "/";
    }

    public static String urlsPath() {
        return rootPath() + "urls";
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
        return urlDetailPath(urlId) + "/checks";
    }
}
