package exercise;

import exercise.exception.IllegalHeaderException;
import exercise.parsers.TableParser;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.Comparator;
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
        var header = table.getHeader(columnName)
                            .orElseThrow(() -> new IllegalHeaderException("Column " + columnName + " not found"));

        Comparator<Table.Row> comparator = getComparator(orderMode, header);
        var sortedRows = table.getRows().stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        return new Table(table.getHeaders(), sortedRows);
    }

    private Comparator<Table.Row> getComparator(OrderBy orderMode, Table.Header header) {
        Comparator<Table.Row> comparator = Comparator.comparing(r -> r.getValues().get(header.getIndex()));
        if (orderMode == OrderBy.DESC){
            comparator = comparator.reversed();
        }
        return comparator;
    }

    public Table join(Table left, Table right, String joinColumnTableLeft, String joinColumnTableRight) { return null; }

}
