package hexlet.code.dto.urls;

import hexlet.code.dto.base.BasePage;
import hexlet.code.model.Url;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UrlsPage extends BasePage {
    private final List<Url> urls;
}
