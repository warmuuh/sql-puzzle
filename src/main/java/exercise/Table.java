package exercise;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Table {

    @Value
    public static class Header {
        String name;
        int index;
    }

    @Value
    public static class Row {
        List<String> values;
    }

    private final List<Header> headers;
    private final List<Row> rows;


    public static Table of(List<String> headerNames, List<List<String>> rowValues) {
        return new Table(initHeaders(headerNames), initRows(rowValues));
    }

    private static List<Row> initRows(List<List<String>> rowValues) {
        return rowValues.stream().map(Row::new).collect(Collectors.toList());
    }

    private static List<Header> initHeaders(List<String> headerNames) {
        List<Header> headers = new ArrayList<>();
        for (int i = 0; i < headerNames.size(); i++) {
            String name = headerNames.get(i);
            headers.add(new Header(name, i));
        }
        return headers;
    }

    public List<Header> getHeaders() {
        return Collections.unmodifiableList(headers);
    }

    public Optional<Header> getHeader(String name) {
        return headers.stream().filter(h -> h.getName().equals(name)).findAny();
    }

    public List<Row> getRows() {
        return Collections.unmodifiableList(rows);
    }
}
