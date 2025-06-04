package hexlet.code.controllers;

import hexlet.code.dto.root.RootPage;
import io.javalin.http.Context;

import static hexlet.code.util.SessionKeys.SESSION_STORE_FLASH_MESSAGE_KEY;
import static io.javalin.rendering.template.TemplateUtil.model;

public class RootController {

    public static void index(Context context) {
        var rootPage = new RootPage();
        rootPage.setFlashMessage(context.consumeSessionAttribute(SESSION_STORE_FLASH_MESSAGE_KEY));
        context.render("index.jte", model("page", rootPage));
    }
}
