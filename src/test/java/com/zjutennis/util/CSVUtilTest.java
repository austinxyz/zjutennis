package com.zjutennis.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CSVUtil Tests")
class CSVUtilTest {

    @Test
    @DisplayName("Should parse simple CSV correctly")
    void testParseSimpleCSV() throws IOException {
        // Arrange
        String csvContent = "Name,Age,City\n" +
                           "John Doe,30,Hangzhou\n" +
                           "Jane Smith,25,Beijing";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        List<Map<String, String>> result = CSVUtil.parseCSV(inputStream);

        // Assert
        assertThat(result).hasSize(2);

        assertThat(result.get(0)).containsEntry("Name", "John Doe");
        assertThat(result.get(0)).containsEntry("Age", "30");
        assertThat(result.get(0)).containsEntry("City", "Hangzhou");

        assertThat(result.get(1)).containsEntry("Name", "Jane Smith");
        assertThat(result.get(1)).containsEntry("Age", "25");
        assertThat(result.get(1)).containsEntry("City", "Beijing");
    }

    @Test
    @DisplayName("Should handle CSV with quoted fields")
    void testParseCSVWithQuotedFields() throws IOException {
        // Arrange
        String csvContent = "Name,Description,Score\n" +
                           "\"John Doe\",\"A great, talented player\",95\n" +
                           "Jane Smith,Regular player,80";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        List<Map<String, String>> result = CSVUtil.parseCSV(inputStream);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).containsEntry("Name", "John Doe");
        assertThat(result.get(0)).containsEntry("Description", "A great, talented player");
        assertThat(result.get(0)).containsEntry("Score", "95");
    }

    @Test
    @DisplayName("Should handle CSV with escaped quotes")
    void testParseCSVWithEscapedQuotes() throws IOException {
        // Arrange
        String csvContent = "Name,Quote\n" +
                           "\"John\",\"He said \"\"Hello\"\"\"\n" +
                           "Jane,Normal text";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        List<Map<String, String>> result = CSVUtil.parseCSV(inputStream);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).containsEntry("Name", "John");
        assertThat(result.get(0)).containsEntry("Quote", "He said \"Hello\"");
    }

    @Test
    @DisplayName("Should handle empty CSV file")
    void testParseEmptyCSV() throws IOException {
        // Arrange
        String csvContent = "";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        List<Map<String, String>> result = CSVUtil.parseCSV(inputStream);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should handle CSV with only headers")
    void testParseCSVOnlyHeaders() throws IOException {
        // Arrange
        String csvContent = "Name,Age,City";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        List<Map<String, String>> result = CSVUtil.parseCSV(inputStream);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should handle CSV with empty lines")
    void testParseCSVWithEmptyLines() throws IOException {
        // Arrange
        String csvContent = "Name,Age,City\n" +
                           "John Doe,30,Hangzhou\n" +
                           "\n" +
                           "Jane Smith,25,Beijing\n" +
                           "\n";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        List<Map<String, String>> result = CSVUtil.parseCSV(inputStream);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).containsEntry("Name", "John Doe");
        assertThat(result.get(1)).containsEntry("Name", "Jane Smith");
    }

    @Test
    @DisplayName("Should handle CSV with missing values")
    void testParseCSVWithMissingValues() throws IOException {
        // Arrange
        String csvContent = "Name,Age,City\n" +
                           "John Doe,,Hangzhou\n" +
                           "Jane Smith,25,";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        List<Map<String, String>> result = CSVUtil.parseCSV(inputStream);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).containsEntry("Name", "John Doe");
        assertThat(result.get(0)).containsEntry("Age", "");
        assertThat(result.get(0)).containsEntry("City", "Hangzhou");

        assertThat(result.get(1)).containsEntry("Name", "Jane Smith");
        assertThat(result.get(1)).containsEntry("Age", "25");
        assertThat(result.get(1)).containsEntry("City", "");
    }

    @Test
    @DisplayName("Should handle CSV with fewer columns than headers")
    void testParseCSVWithFewerColumns() throws IOException {
        // Arrange
        String csvContent = "Name,Age,City,Country\n" +
                           "John Doe,30,Hangzhou\n" +
                           "Jane Smith,25";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        List<Map<String, String>> result = CSVUtil.parseCSV(inputStream);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).containsEntry("Name", "John Doe");
        assertThat(result.get(0)).containsEntry("Age", "30");
        assertThat(result.get(0)).containsEntry("City", "Hangzhou");
        assertThat(result.get(0)).containsEntry("Country", "");

        assertThat(result.get(1)).containsEntry("Name", "Jane Smith");
        assertThat(result.get(1)).containsEntry("Age", "25");
        assertThat(result.get(1)).containsEntry("City", "");
        assertThat(result.get(1)).containsEntry("Country", "");
    }

    @Test
    @DisplayName("Should handle CSV with whitespace in headers and values")
    void testParseCSVWithWhitespace() throws IOException {
        // Arrange
        String csvContent = " Name , Age , City \n" +
                           " John Doe , 30 , Hangzhou \n" +
                           " Jane Smith , 25 , Beijing ";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        List<Map<String, String>> result = CSVUtil.parseCSV(inputStream);

        // Assert
        assertThat(result).hasSize(2);
        // Headers and values should be trimmed
        assertThat(result.get(0)).containsKey("Name");
        assertThat(result.get(0)).containsEntry("Name", "John Doe");
        assertThat(result.get(0)).containsEntry("Age", "30");
    }

    @Test
    @DisplayName("Should handle CSV with special characters")
    void testParseCSVWithSpecialCharacters() throws IOException {
        // Arrange
        String csvContent = "Name,Email,City\n" +
                           "John Doe,john@example.com,Hangzhou\n" +
                           "Jane Smith,jane+test@example.com,Beijing";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        List<Map<String, String>> result = CSVUtil.parseCSV(inputStream);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).containsEntry("Email", "john@example.com");
        assertThat(result.get(1)).containsEntry("Email", "jane+test@example.com");
    }

    @Test
    @DisplayName("Should handle CSV with numeric values")
    void testParseCSVWithNumericValues() throws IOException {
        // Arrange
        String csvContent = "Player ID,Name,UTR Rating,Win Rate\n" +
                           "1,John Doe,12.5,75.5\n" +
                           "2,Jane Smith,8.0,60.0";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        List<Map<String, String>> result = CSVUtil.parseCSV(inputStream);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).containsEntry("Player ID", "1");
        assertThat(result.get(0)).containsEntry("UTR Rating", "12.5");
        assertThat(result.get(0)).containsEntry("Win Rate", "75.5");
    }

    @Test
    @DisplayName("Should handle CSV with dash placeholder values")
    void testParseCSVWithDashPlaceholders() throws IOException {
        // Arrange
        String csvContent = "Player ID,Name,UTR Rating,Status\n" +
                           "1,John Doe,12.5,Verified\n" +
                           "2,Jane Smith,-,-";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        List<Map<String, String>> result = CSVUtil.parseCSV(inputStream);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(1)).containsEntry("UTR Rating", "-");
        assertThat(result.get(1)).containsEntry("Status", "-");
    }

    @Test
    @DisplayName("Should handle CSV with multiline quoted fields")
    void testParseCSVWithMultilineQuotedFields() throws IOException {
        // Arrange
        String csvContent = "Name,Description\n" +
                           "\"John Doe\",\"Line 1\nLine 2\nLine 3\"\n" +
                           "Jane Smith,Simple description";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        List<Map<String, String>> result = CSVUtil.parseCSV(inputStream);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).containsEntry("Name", "John Doe");
        assertThat(result.get(0).get("Description")).contains("Line 1", "Line 2", "Line 3");
    }

    @Test
    @DisplayName("Should handle CSV with comma inside quotes")
    void testParseCSVWithCommaInsideQuotes() throws IOException {
        // Arrange
        String csvContent = "Name,Address,City\n" +
                           "\"Doe, John\",\"123 Main St, Apt 4B\",Hangzhou\n" +
                           "Jane Smith,456 Oak Ave,Beijing";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        List<Map<String, String>> result = CSVUtil.parseCSV(inputStream);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).containsEntry("Name", "Doe, John");
        assertThat(result.get(0)).containsEntry("Address", "123 Main St, Apt 4B");
    }

    @Test
    @DisplayName("Should handle real-world player import CSV format")
    void testParseRealWorldPlayerCSV() throws IOException {
        // Arrange
        String csvContent = "Player ID,Name,Gender,UTR Rating,UTR Status,NTRP Rating,NTRP Status,Dynamic Rating,Win Rate\n" +
                           "1,John Doe,male,12.5,Verified,5.0,Verified,12.8,75%\n" +
                           "2,Jane Smith,female,8.0,Verified,4.0,Verified,8.2,60%\n" +
                           "3,Bob Wilson,male,-,-,-,-,-,-";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        List<Map<String, String>> result = CSVUtil.parseCSV(inputStream);

        // Assert
        assertThat(result).hasSize(3);

        // Check first player
        assertThat(result.get(0)).containsEntry("Player ID", "1");
        assertThat(result.get(0)).containsEntry("Name", "John Doe");
        assertThat(result.get(0)).containsEntry("Gender", "male");
        assertThat(result.get(0)).containsEntry("UTR Rating", "12.5");
        assertThat(result.get(0)).containsEntry("Win Rate", "75%");

        // Check third player with missing values
        assertThat(result.get(2)).containsEntry("Player ID", "3");
        assertThat(result.get(2)).containsEntry("UTR Rating", "-");
        assertThat(result.get(2)).containsEntry("Win Rate", "-");
    }

    @Test
    @DisplayName("Should handle CSV with only whitespace header")
    void testParseCSVWithWhitespaceOnlyHeader() throws IOException {
        // Arrange
        String csvContent = "   \n" +
                           "John,30,Hangzhou";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        List<Map<String, String>> result = CSVUtil.parseCSV(inputStream);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should handle single column CSV")
    void testParseSingleColumnCSV() throws IOException {
        // Arrange
        String csvContent = "Name\n" +
                           "John Doe\n" +
                           "Jane Smith\n" +
                           "Bob Wilson";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        List<Map<String, String>> result = CSVUtil.parseCSV(inputStream);

        // Assert
        assertThat(result).hasSize(3);
        assertThat(result.get(0)).containsEntry("Name", "John Doe");
        assertThat(result.get(1)).containsEntry("Name", "Jane Smith");
        assertThat(result.get(2)).containsEntry("Name", "Bob Wilson");
    }

    @Test
    @DisplayName("Should handle CSV with trailing commas")
    void testParseCSVWithTrailingCommas() throws IOException {
        // Arrange
        String csvContent = "Name,Age,City,\n" +
                           "John Doe,30,Hangzhou,\n" +
                           "Jane Smith,25,Beijing,";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        List<Map<String, String>> result = CSVUtil.parseCSV(inputStream);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).hasSize(4); // 4 keys including empty last column
        assertThat(result.get(0)).containsEntry("Name", "John Doe");
    }
}
