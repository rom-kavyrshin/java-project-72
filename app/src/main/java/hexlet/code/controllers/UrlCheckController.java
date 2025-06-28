package hexlet.code.controllers;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.NotFoundResponse;
import kong.unirest.core.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.CompletableFuture;

@Slf4j
public final class UrlCheckController {

    private UrlCheckController() {
    }

    public static void checkUrlHandler(Context context) {

        int urlId = context.pathParamAsClass("id", Integer.class).getOrDefault(-1);

        Url url;
        try {
            url = UrlRepository.find(urlId).orElseThrow(() -> new NotFoundResponse("Url not found"));
        } catch (SQLException e) {
            throw new InternalServerErrorResponse();
        }

        context.future(() ->
                checkUrl(url)
                        .thenRun(() -> context.redirect(NamedRoutes.urlDetailPath(urlId)))
        );
    }

    public static CompletableFuture<Void> checkUrl(Url url) {

        return Unirest.get(url.getName())
                .asStringAsync()
                .thenApply(response -> {
                    var body = response.getBody();
                    var title = cutTagContent(body, "title");
                    var h1 = cutTagContent(body, "h1");

                    UrlCheck urlCheck = new UrlCheck(
                            response.getStatus(),
                            title,
                            h1,
                            "",
                            url.getId(),
                            new Timestamp(System.currentTimeMillis())
                    );
                    log.info(urlCheck.toString());
                    return urlCheck;
                })
                .thenAccept(urlCheck -> {
                    try {
                        UrlCheckRepository.save(urlCheck);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private static String cutTagContent(String source, String tag) {
        Document doc = Jsoup.parse(source);
        return doc.select(tag).text();
    }
}
