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
    void testDiffFlatJSON() throws Exception {
        var expected = getFixture("src/test/resources/fixtures/flat_result.txt");
        var res = Differ.generate("src/test/resources/fixtures/file1.json", "src/test/resources/fixtures/file2.json");

        assertEquals(expected, res);
    }

    @Test
    void testDiffFlatYML() throws Exception {
        var expected = getFixture("src/test/resources/fixtures/flat_result.txt");
        var res = Differ.generate("src/test/resources/fixtures/file1.yml", "src/test/resources/fixtures/file2.yml");

        assertEquals(expected, res);
    }

    @Test
    void testDiffNestedJSON() throws Exception {
        var expected = getFixture("src/test/resources/fixtures/nested_result.txt");
        var res = Differ.generate("src/test/resources/fixtures/file3.json", "src/test/resources/fixtures/file4.json");

        assertEquals(expected, res);
    }

    @Test
    void testDiffNestedYML() throws Exception {
        var expected = getFixture("src/test/resources/fixtures/nested_result.txt");
        var res = Differ.generate("src/test/resources/fixtures/file3.yml", "src/test/resources/fixtures/file4.yml");

        assertEquals(expected, res);
    }
}
