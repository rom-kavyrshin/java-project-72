package hexlet.code;

import okhttp3.OkHttpClient;

public class TestUtil {

    public static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .cookieJar(new CustomCookieJar())
                .build();
    }
}
