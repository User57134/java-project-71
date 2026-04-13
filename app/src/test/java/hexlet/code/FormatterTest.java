package hexlet.code;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FormatterTest {

    @Test
    void getFormatterTest() throws Exception {
        var formatter = Formatters.getFormatter("stylish");
        assertEquals("hexlet.code.formatters.StylishFormatter", formatter.getClass().getName());

        formatter = Formatters.getFormatter("plain");
        assertEquals("hexlet.code.formatters.PlainFormatter", formatter.getClass().getName());

        formatter = Formatters.getFormatter("json");
        assertEquals("hexlet.code.formatters.JsonFormatter", formatter.getClass().getName());

        formatter = null;
        try {
            formatter = Formatters.getFormatter("other");
        } catch (Exception ex) {
            // nothing
        }
        assertEquals(null, formatter);

        formatter = null;
        try {
            formatter = Formatters.getFormatter(null);
        } catch (Exception ex) {
            // nothing
        }
        assertEquals(null, formatter);

    }
}
