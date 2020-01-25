package exercise;

import exercise.exception.IllegalHeaderException;
import exercise.parsers.TableParser;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Sql {

    enum OrderBy {
        ASC, DESC
    }
    private final TableParser parser;

    public Table readTable(InputStream csvContent) {
        return parser.parseTable(csvContent);
    }	

    public Table orderBy(Table table, String columnName, OrderBy orderMode){
        Table.Header header = retrieveHeader(table, columnName);

        Comparator<Table.Row> comparator = getComparator(orderMode, header);
        var sortedRows = table.getRows().stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        return new Table(table.getHeaders(), sortedRows);
    }

    public Table join(Table left, Table right, String joinColumnTableLeft, String joinColumnTableRight) {
        var leftHeader = retrieveHeader(left, joinColumnTableLeft);
        var rightHeader = retrieveHeader(right, joinColumnTableRight);

        var joinedRows = left.getRows().stream()
                .flatMap(leftRow -> {
                    var joinValue = leftRow.getValues().get(leftHeader.getIndex());
                    return findRowWithValue(right, rightHeader, joinValue)
                            .stream()
                            .map(rightRow -> joinRows(leftRow, rightRow));
                }).collect(Collectors.toList());


        return new Table(joinHeaders(left.getHeaders(), right.getHeaders()), joinedRows);
    }

    private List<Table.Header> joinHeaders(List<Table.Header> leftHeaders, List<Table.Header> rightHeaders) {
        var values = new ArrayList<Table.Header>(leftHeaders.size() + rightHeaders.size());
        values.addAll(leftHeaders);

        for (Table.Header header : rightHeaders) {
            values.add(new Table.Header(header.getName(), header.getIndex() + leftHeaders.size()));
        }
        return values;
    }

    private Table.Row joinRows(Table.Row leftRow, Table.Row rightRow) {
        var values = new ArrayList<String>(leftRow.getValues().size() + rightRow.getValues().size());
        values.addAll(leftRow.getValues());
        values.addAll(rightRow.getValues());
        return new Table.Row(values);
    }


    private List<Table.Row> findRowWithValue(Table table, Table.Header header, String value){
        return table.getRows().stream()
                .filter(r -> r.getValues().get(header.getIndex()).equals(value))
                .collect(Collectors.toList());
    }

    private Table.Header retrieveHeader(Table table, String columnName) {
        return table.getHeader(columnName)
                                .orElseThrow(() -> new IllegalHeaderException("Column " + columnName + " not found"));
    }

    private Comparator<Table.Row> getComparator(OrderBy orderMode, Table.Header header) {
        Comparator<Table.Row> comparator = Comparator.comparing(r -> r.getValues().get(header.getIndex()));
        if (orderMode == OrderBy.DESC){
            comparator = comparator.reversed();
        }
        return comparator;
    }



}
