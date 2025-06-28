package hexlet.code.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@EqualsAndHashCode
@ToString
@Getter
@Setter
public class Url {
    private Long id;
    private String name;
    private Timestamp createdAt;

    public Url(String name, Timestamp timestamp) {
        this.name = name;
        this.createdAt = timestamp;
    }
}
