package hexlet.code.utiltest;

import hexlet.code.util.Util;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UtilTest {

    @Test
    void testUrlRemoveAll() {
        assertThat(Util.substring("<title>Something</title>", "<title>", "</title>")).isEqualTo("Something");
        assertThat(Util.substring("<title>Something<title>", "<title>", "</title>")).isEmpty();
        assertThat(Util.substring("<someprevious><title href=\"\">Something</title>", "<title>", "</title>"))
                .isEmpty();
        assertThat(Util.substring("</title>Something<title>", "<title>", "</title>")).isEmpty();
    }
}
