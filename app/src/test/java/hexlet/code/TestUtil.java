package hexlet.code;

import okhttp3.OkHttpClient;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtil {

    public static Path getFixturePath(String... path) {
        String[] combined = new String[path.length + 2];
        combined[0] = "test";
        combined[1] = "resources";
        System.arraycopy(path, 0, combined, 2, path.length);
        return Paths.get("src", combined)
                .toAbsolutePath().normalize();
    }

    public static String readFixture(String... stringPath) throws Exception {
        var path = getFixturePath(stringPath);
        return Files.readString(path).trim();
    }

    public static File createFixtureFile(String... stringPath) {
        var path = getFixturePath(stringPath);
        return path.toFile();
    }

    public static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .cookieJar(new CustomCookieJar())
                .build();
    }
}
