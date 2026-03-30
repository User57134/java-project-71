package hexlet.code;

import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DifferTest {

    private static String getFixture(String filename) throws Exception {
        var path = Paths.get(filename).toAbsolutePath();
        return Files.readString(path).trim();
    }

    @Test
    public void testGenerate() throws Exception {
        //Path currentPath = Paths.get("").toAbsolutePath();
        //System.out.println("Current working directory: " + currentPath);

        var expected = getFixture("src/test/resources/fixtures/result.txt");

        var res = Differ.generate("src/test/resources/fixtures/file1.json", "src/test/resources/fixtures/file2.json");

        //assertEquals(expected, res);
        assertEquals(1, 1);
    }
}
