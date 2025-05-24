package hexlet.code.controllers;

import hexlet.code.dto.root.RootPage;
import io.javalin.http.Context;

import static io.javalin.rendering.template.TemplateUtil.model;

public class RootController {

    public static void index(Context context) {
        var rootPage = new RootPage();
        context.render("index.jte", model("page", rootPage));
    }
}
