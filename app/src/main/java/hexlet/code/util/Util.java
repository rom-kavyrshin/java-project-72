package hexlet.code.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public final class Util {

    private Util() {
    }

    private static final String PROTOCOL_DELIMETER = "://";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss");

    public static String getEnv(String key, String defaultValue) {
        return System.getenv().getOrDefault(key, defaultValue);
    }

    public static String shrinkUrl(String urlString) throws URISyntaxException, MalformedURLException {
        var url = new URI(urlString).toURL();
        return url.getProtocol()
                + PROTOCOL_DELIMETER
                + url.getHost()
                + (url.getPort() == -1 ? "" : ":" + url.getPort());
    }

    public static String formatTimestamp(Timestamp timestamp) {
        return DATE_TIME_FORMATTER.format(timestamp.toLocalDateTime());
    }

    public static String substring(String source, String beginString, String endString) {
        var begin = source.indexOf(beginString);
        var end = source.indexOf(endString);

        if (begin == -1 || end == -1 || begin > end) {
            return "";
        }

        begin += beginString.length();

        return source.substring(begin, end);
    }
}
