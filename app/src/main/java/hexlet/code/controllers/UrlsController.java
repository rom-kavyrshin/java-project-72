package hexlet.code.controllers;

import hexlet.code.dto.base.FlashMessage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.validation.ValidationException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

import static hexlet.code.util.SessionKeys.SESSION_STORE_FLASH_MESSAGE_KEY;

public class UrlsController {

    public static void create(Context ctx) throws SQLException {
        try {
            var urlString = ctx.formParamAsClass("url", String.class)
                    .check(value -> !value.isEmpty(), "Ссылка не должна быть пустой")
                    .get();
            var url = new URI(urlString).toURL();
            urlString = url.getProtocol() + url.getHost() + (url.getPort() == -1 ? "" : url.getPort());
            var urlModel = new Url(urlString);
            UrlRepository.save(urlModel);
            ctx.sessionAttribute(SESSION_STORE_FLASH_MESSAGE_KEY, new FlashMessage("Страница успешно добавлена", true));

            ctx.redirect(NamedRoutes.rootPath());
        } catch (ValidationException e) {
            ctx.sessionAttribute(SESSION_STORE_FLASH_MESSAGE_KEY, new FlashMessage("Ошибка валидации", false));

            ctx.redirect(NamedRoutes.rootPath());
        } catch (URISyntaxException | MalformedURLException | IllegalArgumentException e) {
            ctx.sessionAttribute(SESSION_STORE_FLASH_MESSAGE_KEY, new FlashMessage("Некорректный URL", false));

            ctx.redirect(NamedRoutes.rootPath());
        }
    }
}
