package hexlet.code.dto.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FlashMessage {
    private String message;
    private boolean isSuccess;
}
