package hexlet.code;

import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DifferTest {

    private static String getFixture(String filename) throws Exception {
        var path = Paths.get(filename).toAbsolutePath();
        return Files.readString(path).trim();
    }

    @Test
    void testGenerate() throws Exception {
        var expected = getFixture("src/test/resources/fixtures/result.txt");
        var res = Differ.generate("src/test/resources/fixtures/file1.json", "src/test/resources/fixtures/file2.json");

        assertEquals(expected, res);
    }
}
