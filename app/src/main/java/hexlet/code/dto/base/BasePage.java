package hexlet.code.dto.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BasePage {
    private FlashMessage flashMessage;
}
