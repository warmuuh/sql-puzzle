package exercise.commands;

import exercise.Table;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class JoinCommand extends AbstractCommand {

    private final Table left;
    private final Table right;
    private final String joinColumnTableLeft;
    private final String joinColumnTableRight;

    @Override
    public Table execute() {
        var leftHeader = retrieveHeader(left, joinColumnTableLeft);
        var rightHeader = retrieveHeader(right, joinColumnTableRight);

        var joinedRows = left.getRows().stream()
                .flatMap(leftRow -> {
                    var joinValue = leftRow.getValue(leftHeader);
                    return findRowWithValue(right, rightHeader, joinValue)
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

    private Stream<Table.Row> findRowWithValue(Table table, Table.Header header, String value){
        return table.getRows().stream()
                .filter(r -> r.getValue(header).equals(value));
    }
}
