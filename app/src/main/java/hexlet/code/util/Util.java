package hexlet.code.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class Util {

    private static final String PROTOCOL_DELIMETER = "://";

    public static String getEnv(String key, String defaultValue) {
        return System.getenv().getOrDefault(key, defaultValue);
    }

    public static String shrinkUrl(String urlString) throws URISyntaxException, MalformedURLException {
        var url = new URI(urlString).toURL();
        return url.getProtocol() + PROTOCOL_DELIMETER + url.getHost() + (url.getPort() == -1 ? "" : url.getPort());
    }
}
