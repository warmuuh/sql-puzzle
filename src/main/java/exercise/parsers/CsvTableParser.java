package exercise.parsers;

import exercise.Table;
import exercise.exception.IllegalHeaderException;
import exercise.exception.ParseException;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CsvTableParser implements TableParser {

    private final Pattern separator;

    public CsvTableParser() {
        this(",");
    }

    public CsvTableParser(String separatorStr) {
        this.separator = Pattern.compile(separatorStr, Pattern.LITERAL);
    }


    @Override
    public Table parseTable(InputStream csvContent) {
        Objects.requireNonNull(csvContent, "no input stream given");

        try(var in = csvContent){
            List<List<String>> tokenizedLines = tokenizeLines(in);
            List<String> headers = extractHeaders(tokenizedLines);
            List<List<String>> rows = extractRows(tokenizedLines);
            return new Table(headers, rows);
        } catch (IOException e) {
            throw new ParseException("Failed to parse CSV content", e);
        }
    }

    private List<List<String>> extractRows(List<List<String>> tokenizedLines) {
        return tokenizedLines.stream().skip(1).collect(Collectors.toList());
    }

    private List<String> extractHeaders(List<List<String>> tokenizedLines) {
        var headers = tokenizedLines.stream().findFirst().orElse(Collections.emptyList());
        if (headers.stream().anyMatch(String::isBlank))
            throw new IllegalHeaderException("Headers cannot be empty");

        return headers;
    }

    private List<List<String>> tokenizeLines(InputStream csvContent) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(csvContent));
        return reader.lines()
                .map(separator::split)
                .map(Arrays::asList)
                .map(this::trimValues)
                .collect(Collectors.toList());
    }

    private List<String> trimValues(List<String> values) {
        return values.stream().map(String::trim).collect(Collectors.toList());
    }

}
