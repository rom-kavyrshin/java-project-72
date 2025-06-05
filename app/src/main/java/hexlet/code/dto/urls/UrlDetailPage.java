package hexlet.code.dto.urls;

import hexlet.code.dto.base.BasePage;
import hexlet.code.model.Url;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UrlDetailPage extends BasePage {
    private final Url url;
}
