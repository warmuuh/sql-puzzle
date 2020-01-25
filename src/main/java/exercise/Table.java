package exercise;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

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
        int index;
    }

    private final List<Header> headers = new ArrayList<>();
    private final List<Row> rows  = new ArrayList<>();


    public Table(List<String> headerNames, List<List<String>> rowValues) {
        initHeaders(headerNames);
        initRows(rowValues);
    }

    private void initRows(List<List<String>> rowValues) {
        for (int i = 0; i < rowValues.size(); i++) {
            List<String> values = rowValues.get(i);
            rows.add(new Row(values, i));
        }
    }

    private void initHeaders(List<String> headerNames) {
        for (int i = 0; i < headerNames.size(); i++) {
            String name = headerNames.get(i);
            headers.add(new Header(name, i));
        }
    }


    public List<Header> getHeaders() {
        return Collections.unmodifiableList(headers);
    }

    public List<Row> getRows() {
        return Collections.unmodifiableList(rows);
    }
}
