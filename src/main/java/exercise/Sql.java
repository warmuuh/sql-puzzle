package exercise;

import exercise.commands.JoinCommand;
import exercise.commands.OrderByCommand;
import exercise.parsers.TableParser;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;

@RequiredArgsConstructor
public class Sql {

    public enum OrderBy {
        ASC, DESC
    }

    private final TableParser parser;

    public Table readTable(InputStream csvContent) {
        return parser.parseTable(csvContent);
    }	

    public Table orderBy(Table table, String columnName, OrderBy orderMode){
        var command = new OrderByCommand(table, columnName, orderMode);
        return command.execute();
    }

    public Table join(Table left, Table right, String joinColumnTableLeft, String joinColumnTableRight) {
        var command = new JoinCommand(left, right, joinColumnTableLeft, joinColumnTableRight);
        return command.execute();
    }

}
