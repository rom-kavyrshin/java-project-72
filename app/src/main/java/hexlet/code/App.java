package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.controllers.RootController;
import hexlet.code.controllers.UrlCheckController;
import hexlet.code.controllers.UrlsController;
import hexlet.code.repository.BaseRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import kong.unirest.core.Unirest;
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

        Unirest.config()
                .connectTimeout(1000);

        try {
            initDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.get(NamedRoutes.rootPath(), RootController::index);

        app.get(NamedRoutes.urlsPath(), UrlsController::index);
        app.get(NamedRoutes.urlDetailPath("{id}"), UrlsController::show);
        app.post(NamedRoutes.urlsPath(), UrlsController::create);
        app.post(NamedRoutes.urlCheckPath("{id}"), UrlCheckController::checkUrlHandler);

        return app;
    }

    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(getPort());
    }

    private static TemplateEngine createTemplateEngine() {
        return TemplateEngine.create(
                new ResourceCodeResolver("templates", App.class.getClassLoader()),
                ContentType.Html
        );
    }

    private static void initDatabase() throws SQLException {
        var jdbcUrl = getJdbcUrl();
        var isDbPostgres = jdbcUrl.toLowerCase().startsWith("jdbc:postgresql");
        var schemaName = isDbPostgres ? "schema_postgresql.sql" : "schema.sql";

        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);
        BaseRepository.dataSource = new HikariDataSource(hikariConfig);

        var schema = App.class.getClassLoader().getResourceAsStream(schemaName);
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
