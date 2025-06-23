package hexlet.code.dto.urls;

import hexlet.code.dto.base.BasePage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UrlDetailPage extends BasePage {
    private final Url url;
    private final List<UrlCheck> urlChecks;
}
