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
    public void testUrlRemoveAll() throws SQLException {
        UrlRepository.save(new Url("https://first.com", Timestamp.from(Instant.now())));
        UrlRepository.save(new Url("https://second.com", Timestamp.from(Instant.now())));
        UrlRepository.removeAll();
        UrlRepository.save(new Url("https://third.com", Timestamp.from(Instant.now())));

        var list = UrlRepository.getAll();

        assertThat(list.size()).isEqualTo(1);
        assertThat(list.getFirst().getId()).isEqualTo(3);
        assertThat(list.getFirst().getName()).isEqualTo("https://third.com");
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
    public void testShow() {
        JavalinTest.test(app, testConfig, (server, client) -> {
            var requestBody = "url=https://ya.ru";

            try (var response = client.post(NamedRoutes.urlsPath(), requestBody)) {
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string()).contains("Страница успешно добавлена");
            }

            try (var response = client.get(NamedRoutes.urlPath(1))) {
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string()).contains("https://ya.ru");
            }
        });
    }

    @Test
    public void testShowNotFound() {
        JavalinTest.test(app, testConfig, (server, client) -> {
            try (var response = client.get(NamedRoutes.urlPath(1))) {
                assertThat(response.code()).isEqualTo(404);
                assertThat(response.body().string()).contains("Url not found");
            }
        });
    }
}
