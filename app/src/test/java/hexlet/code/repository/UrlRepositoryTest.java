package hexlet.code.repository;

import hexlet.code.App;
import hexlet.code.model.Url;
import io.javalin.Javalin;
import io.javalin.testtools.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import static hexlet.code.TestUtil.getOkHttpClient;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UrlRepositoryTest {

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
}
