package hexlet.code;

import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import io.javalin.testtools.TestConfig;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static hexlet.code.TestUtil.getOkHttpClient;
import static hexlet.code.TestUtil.readFixture;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UrlCheckTest {

    private Javalin app;
    private TestConfig testConfig;
    private final List<MockWebServer> mockWebServers = new ArrayList<>();

    @BeforeEach
    final void setUp() throws Exception {
        app = App.getApp();
        testConfig = new TestConfig(false, true, getOkHttpClient());
        prepareMockServers();
    }

    private void prepareMockServers() throws Exception {
        MockWebServer server = new MockWebServer();
        MockWebServer server2 = new MockWebServer();
        MockWebServer server3 = new MockWebServer();

        server.enqueue(new MockResponse().setBody(readFixture("url_check", "first_site.html")));
        server2.enqueue(new MockResponse().setBody(readFixture("url_check", "second_site.html")));
        server3.enqueue(new MockResponse().setBody(readFixture("url_check", "third_site.html")));

        mockWebServers.add(server);
        mockWebServers.add(server2);
        mockWebServers.add(server3);

        for (var item : mockWebServers) {
            item.start();
        }
    }

    @Test
    void testChecks() {
        JavalinTest.test(app, testConfig, (server, client) -> {
            // Добавляем все серверы на проверку
            for (var mockServer : mockWebServers) {
                var mockServerUrl = mockServer.url("").toString();
                var requestBody = "url=" + mockServerUrl;

                try (var response = client.post(NamedRoutes.urlsPath(), requestBody)) {
                    assertThat(response.code()).isEqualTo(200);
                    assertThat(response.body().string()).contains("Страница успешно добавлена");
                }
            }

            // Проверяем что добавились все серверы на странице /urls
            try (var response = client.get(NamedRoutes.urlsPath())) {
                assertThat(response.code()).isEqualTo(200);

                assertThat(response.body().string())
                        .contains(mockWebServers.stream()
                                .map(it -> {
                                    var url = it.url("").toString();
                                    return url.substring(0, url.length() - 1);
                                })
                                .toList());
            }

            System.out.println("!!!!!!!!!!!!!!!!!!!!!!! UrlRepository.getAll " + UrlRepository.getAll());

            // Вызываем check для каждого сайта
            for (int i = 1; i <= mockWebServers.size(); i++) {
                try (var response = client.post(NamedRoutes.urlCheckPath(i))) {
                    assertThat(response.code()).isEqualTo(200);
                }
            }

            try (var response = client.get(NamedRoutes.urlDetailPath(1))) {
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string())
                        .contains("200")
                        .contains("pizza store")
                        .contains("Buy pizza");
            }

            try (var response = client.get(NamedRoutes.urlDetailPath(2))) {
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string())
                        .contains("200")
                        .contains("Smithereens")
                        .contains("Share ur moments with friends");
            }

            try (var response = client.get(NamedRoutes.urlDetailPath(3))) {
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string())
                        .contains("200")
                        .contains("Napster")
                        .contains("Listen music without restrictions");
            }
        });
    }

    @AfterEach
    final void tearDown() throws SQLException, IOException {
        UrlCheckRepository.removeAll();
        UrlRepository.removeAll();

        for (var server : mockWebServers) {
            server.close();
        }
    }
}
