package hexlet.code.controllers;

import hexlet.code.dto.base.FlashMessage;
import hexlet.code.dto.urls.UrlDetailPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.exception.SiteAlreadyPresentException;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.Util;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

import static hexlet.code.util.SessionKeys.SESSION_STORE_FLASH_MESSAGE_KEY;
import static io.javalin.rendering.template.TemplateUtil.model;

@Slf4j
public class UrlsController {

    public static void index(Context ctx) {
        try {
            UrlsPage urlsPage = new UrlsPage(UrlRepository.getAll());
            urlsPage.setFlashMessage(ctx.consumeSessionAttribute(SESSION_STORE_FLASH_MESSAGE_KEY));
            ctx.render("urls/index.jte", model("page", urlsPage));
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new InternalServerErrorResponse();
        }
    }

    public static void create(Context ctx) throws SQLException {
        try {
            var urlString = ctx.formParamAsClass("url", String.class)
                    .check(value -> !value.isEmpty(), "Ссылка не должна быть пустой")
                    .get();
            urlString = Util.shrinkUrl(urlString);

            if (UrlRepository.findByUrl(urlString).isPresent()) {
                throw new SiteAlreadyPresentException();
            }
            var urlModel = new Url(urlString, new Timestamp(System.currentTimeMillis()));
            UrlRepository.save(urlModel);
            ctx.sessionAttribute(SESSION_STORE_FLASH_MESSAGE_KEY, new FlashMessage("Страница успешно добавлена", true));

            ctx.redirect(NamedRoutes.rootPath());
        } catch (ValidationException e) {
            ctx.sessionAttribute(SESSION_STORE_FLASH_MESSAGE_KEY, new FlashMessage("Ошибка валидации", false));

            ctx.redirect(NamedRoutes.rootPath());
        } catch (URISyntaxException | MalformedURLException | IllegalArgumentException e) {
            ctx.sessionAttribute(SESSION_STORE_FLASH_MESSAGE_KEY, new FlashMessage("Некорректный URL", false));

            ctx.redirect(NamedRoutes.rootPath());
        } catch (SiteAlreadyPresentException e) {
            ctx.sessionAttribute(SESSION_STORE_FLASH_MESSAGE_KEY, new FlashMessage("Страница уже существует", false));

            ctx.redirect(NamedRoutes.rootPath());
        }
    }

    public static void show(Context context) {
        int urlId = context.pathParamAsClass("id", Integer.class).getOrDefault(-1);

        Optional<Url> urlOptional;
        try {
            urlOptional = UrlRepository.find(urlId);
        } catch (SQLException e) {
            throw new InternalServerErrorResponse();
        }

        urlOptional.ifPresentOrElse((it) -> {
            var page = new UrlDetailPage(it);
            page.setFlashMessage(context.consumeSessionAttribute(SESSION_STORE_FLASH_MESSAGE_KEY));
            context.render("urls/show.jte", model("page", page));
        }, () -> {
            throw new NotFoundResponse("Url not found");
        });
    }
}
