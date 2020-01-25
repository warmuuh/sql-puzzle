package exercise.commands;

import exercise.Sql;
import exercise.Table;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrderByCommand extends AbstractCommand {

    private final Table table;
    private final String columnName;
    private final Sql.OrderBy orderMode;

    public Table execute() {
        Table.Header header = retrieveHeader(table, columnName);

        Comparator<Table.Row> comparator = getComparator(orderMode, header);
        var sortedRows = table.getRows().stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        return new Table(table.getHeaders(), sortedRows);
    }

    private Comparator<Table.Row> getComparator(Sql.OrderBy orderMode, Table.Header header) {
        Comparator<Table.Row> comparator = Comparator.comparing(r -> r.getValues().get(header.getIndex()));
        if (orderMode == Sql.OrderBy.DESC){
            comparator = comparator.reversed();
        }
        return comparator;
    }

}
