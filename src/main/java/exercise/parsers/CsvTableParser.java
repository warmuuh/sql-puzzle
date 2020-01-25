package exercise.parsers;

import exercise.Table;
import exercise.exception.IllegalHeaderException;
import exercise.exception.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.function.Predicate.*;

public class CsvTableParser implements TableParser {

    private final Pattern separator;
    private final InvalidRowHandler invalidRowHandler;

    public CsvTableParser() {
        this(",", row -> Collections.emptyList());
    }

    public CsvTableParser(String separatorStr, InvalidRowHandler invalidRowHandler) {
        this.separator = Pattern.compile(separatorStr, Pattern.LITERAL);
        this.invalidRowHandler = invalidRowHandler;
    }


    @Override
    public Table parseTable(InputStream csvContent) {
        Objects.requireNonNull(csvContent, "no input stream given");

        try(var in = csvContent){
            List<List<String>> tokenizedLines = tokenizeLines(in);
            List<String> headers = extractHeaders(tokenizedLines);
            List<List<String>> rows = extractRows(tokenizedLines, headers.size());
            return Table.of(headers, rows);
        } catch (IOException e) {
            throw new ParseException("Failed to parse CSV content", e);
        }
    }

    private List<List<String>> extractRows(List<List<String>> tokenizedLines, int columnCount) {
        return tokenizedLines.stream()
                .skip(1)
                .map(row -> validateRow(columnCount, row))
                .filter(not(List::isEmpty))
                .collect(Collectors.toList());
    }

    private List<String> validateRow(int columnCount, List<String> row) {
        if (row.size() != columnCount){
            return invalidRowHandler.recoverRow(row);
        }
        return row;
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
                .map(line -> separator.split(line, -1))
                .map(Arrays::asList)
                .map(this::trimValues)
                .collect(Collectors.toList());
    }

    private List<String> trimValues(List<String> values) {
        return values.stream().map(String::trim).collect(Collectors.toList());
    }

    @FunctionalInterface
    public interface InvalidRowHandler {
        List<String> recoverRow(List<String> invalidRow);
    }

}
