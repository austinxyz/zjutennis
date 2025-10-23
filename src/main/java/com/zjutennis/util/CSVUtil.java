package com.zjutennis.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class CSVUtil {

    public static List<Map<String, String>> parseCSV(InputStream inputStream) throws IOException {
        List<Map<String, String>> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String headerLine = reader.readLine();
            if (headerLine == null || headerLine.trim().isEmpty()) {
                return result;
            }

            List<String> headers = parseCSVLine(headerLine);

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                List<String> values = parseCSVLine(line);
                Map<String, String> row = new HashMap<>();

                for (int i = 0; i < headers.size(); i++) {
                    String value = i < values.size() ? values.get(i) : "";
                    row.put(headers.get(i).trim(), value.trim());
                }

                result.add(row);
            }
        }

        return result;
    }

    private static List<String> parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                values.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }

        values.add(current.toString());
        return values;
    }
}
