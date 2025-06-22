package hexlet.code.utiltest;

import hexlet.code.util.Util;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UtilTest {

    @Test
    public void testUrlRemoveAll() {
        assertThat(Util.substring("<title>Something</title>", "<title>", "</title>")).isEqualTo("Something");
        assertThat(Util.substring("<title>Something<title>", "<title>", "</title>")).isEqualTo("");
        assertThat(Util.substring("</title>Something<title>", "<title>", "</title>")).isEqualTo("");
    }
}
