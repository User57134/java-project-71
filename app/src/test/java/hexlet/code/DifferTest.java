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
    void getFileExtensionTest() {
        var filename = "file.JSON";
        var expected = "json";
        var actual = Differ.getFileExtension(filename);

        assertEquals(expected, actual);

        filename = "file.Yml";
        expected = "yml";
        actual = Differ.getFileExtension(filename);

        assertEquals(expected, actual);
    }

    @Test
    void testDiffJsonStylish() throws Exception {
        var expected = getFixture("src/test/resources/fixtures/result.txt");
        var res = Differ.generate(
                "src/test/resources/fixtures/file1.json",
                "src/test/resources/fixtures/file2.json",
                "stylish");

        assertEquals(expected, res);
    }

    @Test
    void testDiffYmlStylish() throws Exception {
        var expected = getFixture("src/test/resources/fixtures/result.txt");
        var res = Differ.generate(
                "src/test/resources/fixtures/file1.yml",
                "src/test/resources/fixtures/file2.yml",
                "stylish");

        assertEquals(expected, res);
    }

    @Test
    void testDiffJsonPlain() throws Exception {
        var expected = getFixture("src/test/resources/fixtures/result.txt");
        var res = Differ.generate(
                "src/test/resources/fixtures/file1.json",
                "src/test/resources/fixtures/file2.json",
                "plain");

        assertEquals(expected, res);
    }

    @Test
    void testDiffYmlPlain() throws Exception {
        var expected = getFixture("src/test/resources/fixtures/result.txt");
        var res = Differ.generate(
                "src/test/resources/fixtures/file1.yml",
                "src/test/resources/fixtures/file2.yml",
                "plain");

        assertEquals(expected, res);
    }
}
