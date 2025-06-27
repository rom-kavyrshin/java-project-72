package hexlet.code;

import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.testtools.TestConfig;
import kong.unirest.core.Unirest;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static hexlet.code.TestUtil.getOkHttpClient;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UrlCheckTest {

    private Javalin app;
    private TestConfig testConfig;

    OkHttpClient client;

    @BeforeEach
    public final void setUp() throws SQLException {
        client = new OkHttpClient();

        app = App.getApp();
        testConfig = new TestConfig(false, true, getOkHttpClient());
        UrlRepository.removeAll();
    }

    @Test
    public void testExample() throws IOException, InterruptedException {
        MockWebServer server = new MockWebServer();
        MockWebServer server2 = new MockWebServer();

        server.enqueue(new MockResponse().setBody("hello, world!"));
        server.enqueue(new MockResponse().setBody("sup, bra?"));
        server.enqueue(new MockResponse().setBody("yo dog"));

        server2.enqueue(new MockResponse().setBody("server 2 is speaking"));

        server.start();
        server2.start();

        System.out.println("@!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! server1url " + server.url(""));
        System.out.println("@!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! server2url " + server2.url(""));

        var mockServerUrl = server.url("").toString();

        var result = Unirest.get(mockServerUrl).asString().getBody();
        var result2 = Unirest.get(server2.url("").toString()).asString().getBody();

        var path = server.takeRequest().getPath();
        System.out.println("@!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! path " + path);
        System.out.println("@!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! result2 " + result2);

        assertThat(result).isEqualTo("hello, world!");

        server.close();
        server2.close();
    }

    @AfterEach
    public final void tearDown() throws SQLException {
        app = App.getApp();
        testConfig = new TestConfig(false, true, getOkHttpClient());
        UrlRepository.removeAll();
    }
}
