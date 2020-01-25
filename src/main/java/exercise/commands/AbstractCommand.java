package exercise.commands;

import exercise.Table;
import exercise.exception.IllegalHeaderException;

public abstract class AbstractCommand implements SqlCommand {

    protected Table.Header retrieveHeader(Table table, String columnName) {
        return table.getHeader(columnName)
                .orElseThrow(() -> new IllegalHeaderException("Column " + columnName + " not found"));
    }
}
