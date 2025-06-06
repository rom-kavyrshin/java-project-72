package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AppTest {

    private Javalin app;

    @BeforeEach
    public final void setUp() throws SQLException {
        app = App.getApp();
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
    public void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://ya.ru";
            try (var response = client.post(NamedRoutes.urlsPath(), requestBody)) {
                assertThat(response.code()).isEqualTo(200);
                var urlsResponse = client.get(NamedRoutes.urlsPath());
                assertThat(urlsResponse.body().string()).contains("https://ya.ru");
            }
        });
    }
}
