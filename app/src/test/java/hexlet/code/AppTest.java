package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import io.javalin.testtools.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import static hexlet.code.TestUtil.getOkHttpClient;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AppTest {

    private Javalin app;
    private TestConfig testConfig;

    @BeforeEach
    public final void setUp() throws SQLException {
        app = App.getApp();
        testConfig = new TestConfig(false, true, getOkHttpClient());
        UrlRepository.removeAll();
    }

    @Test
    public void testRootPath() {
        JavalinTest.test(app, (server, client) -> {
            try (var response = client.get(NamedRoutes.rootPath())) {
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string()).contains("Анализатор страниц");
            }
        });
    }

    @Test
    public void testCreateUrl() {
        JavalinTest.test(app, testConfig, (server, client) -> {
            var requestBody = "url=https://ya.ru";
            try (var response = client.post(NamedRoutes.urlsPath(), requestBody)) {
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string()).contains("Страница успешно добавлена");
            }
            var urlsResponse = client.get(NamedRoutes.urlsPath());
            assertThat(urlsResponse.body().string()).contains("https://ya.ru");
        });
    }

    @Test
    public void testCreateUrlWithParams() {
        JavalinTest.test(app, testConfig, (server, client) -> {
            var requestBody = "url=https://ya.ru:5050?param1=42";
            try (var response = client.post(NamedRoutes.urlsPath(), requestBody)) {
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string()).contains("Страница успешно добавлена");
            }
            var urlsResponse = client.get(NamedRoutes.urlsPath());
            assertThat(urlsResponse.body().string()).contains("https://ya.ru:5050");
        });
    }

    @Test
    public void testUrlSyntaxException() {
        JavalinTest.test(app, testConfig, (server, client) -> {
            var requestBody = "url=httpsexample.com";
            try (var response = client.post(NamedRoutes.urlsPath(), requestBody)) {
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string()).contains("Некорректный URL");
            }
        });
    }

    @Test
    public void testUrlValidationException() {
        JavalinTest.test(app, testConfig, (server, client) -> {
            var requestBody = "url=";
            try (var response = client.post(NamedRoutes.urlsPath(), requestBody)) {
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string()).contains("Ошибка валидации");
            }
        });
    }

    @Test
    public void testCreateSameUrlTwice() {
        JavalinTest.test(app, testConfig, (server, client) -> {
            var requestBody = "url=https://ya.ru";

            try (var response = client.post(NamedRoutes.urlsPath(), requestBody)) {
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string()).contains("Страница успешно добавлена");
            }

            try (var response = client.post(NamedRoutes.urlsPath(), requestBody)) {
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string()).contains("Страница уже существует");
            }
        });
    }

    @Test
    public void testUrls() {
        JavalinTest.test(app, testConfig, (server, client) -> {
            UrlRepository.save(new Url("https://ya.ru", Timestamp.from(Instant.now())));
            UrlRepository.save(new Url("https://example.com", Timestamp.from(Instant.now())));
            UrlRepository.save(new Url("https://vk.com", Timestamp.from(Instant.now())));

            var urlsResponse = client.get(NamedRoutes.urlsPath()).body().string();
            assertThat(urlsResponse).contains("https://ya.ru");
            assertThat(urlsResponse).contains("https://example.com");
            assertThat(urlsResponse).contains("https://vk.com");
        });
    }

    @Test
    public void testShow() {
        JavalinTest.test(app, testConfig, (server, client) -> {
            var requestBody = "url=https://ya.ru";

            try (var response = client.post(NamedRoutes.urlsPath(), requestBody)) {
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string()).contains("Страница успешно добавлена");
            }

            try (var response = client.get(NamedRoutes.urlDetailPath(1))) {
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string()).contains("https://ya.ru");
            }
        });
    }

    @Test
    public void testShowNotFound() {
        JavalinTest.test(app, testConfig, (server, client) -> {
            try (var response = client.get(NamedRoutes.urlDetailPath(1))) {
                assertThat(response.code()).isEqualTo(404);
                assertThat(response.body().string()).contains("Url not found");
            }
        });
    }
}
