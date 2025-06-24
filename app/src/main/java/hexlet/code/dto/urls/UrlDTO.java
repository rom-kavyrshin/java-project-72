package hexlet.code.dto.urls;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class UrlDTO {
    private Long id;
    private String name;
    private Integer lastCheckStatusCode;
    private Timestamp lastCheckTime;
}
