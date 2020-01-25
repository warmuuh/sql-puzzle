package exercise;

import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ToString
@Getter
@EqualsAndHashCode
public class Table {

    private final List<Header> headers;
    private final List<Row> rows;

    public static Table of(List<String> headerNames, List<List<String>> rowValues) {
        return new Table(initHeaders(headerNames), initRows(rowValues));
    }

    public Table(List<Header> headers, List<Row> rows) {
        this.headers = Collections.unmodifiableList(headers);
        this.rows = Collections.unmodifiableList(rows);
    }

    public Optional<Header> getHeader(String name) {
        return headers.stream().filter(h -> h.getName().equals(name)).findAny();
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

    @Value
    public static class Header {
        String name;
        int index;
    }

    @Value
    public static class Row {
        List<String> values;

        public String getValue(Header header){
            return values.get(header.getIndex());
        }
    }
}
