package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.repository.BaseRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.stream.Collectors;

import static hexlet.code.util.Util.getEnv;

@Slf4j
public class App {

    public static Javalin getApp() {
        log.info("jdbc url: {}", getJdbcUrl());
        log.info("port: {}", getPort());

        try {
            initDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });

        app.get(NamedRoutes.rootPath(), context -> context.result("Hello world"));

        return app;
    }

    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(getPort());
    }

    private static void initDatabase() throws SQLException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getJdbcUrl());
        BaseRepository.dataSource = new HikariDataSource(hikariConfig);

        var schema = App.class.getClassLoader().getResourceAsStream("schema.sql");
        var sql = new BufferedReader(new InputStreamReader(schema)).lines().collect(Collectors.joining("\n"));

        try (var connection = BaseRepository.dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private static int getPort() {
        String port = getEnv("PORT", "7070");
        return Integer.parseInt(port);
    }

    private static String getJdbcUrl() {
        return getEnv("JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1");
    }
}
