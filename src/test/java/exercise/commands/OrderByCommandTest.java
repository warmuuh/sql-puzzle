package exercise.commands;

import exercise.Sql;
import exercise.Table;
import exercise.exception.IllegalHeaderException;
import exercise.parsers.TableParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class OrderByCommandTest {


    @Test
    void shouldThrowOnInvalidColumnName(){
        var table = Table.of(List.of("id", "value"), List.of());

        assertThatThrownBy(() -> new OrderByCommand(table, "unknownColumn", Sql.OrderBy.ASC).execute())
                .isInstanceOf(IllegalHeaderException.class);
    }

    @Test
    void shouldSortGivenTableAscending(){
        var table = Table.of(List.of("id", "value"), List.of(
                List.of("3", "value3"),
                List.of("4", "value4"),
                List.of("1", "value1"),
                List.of("2", "value2")
        ));

        var command = new OrderByCommand(table, "id", Sql.OrderBy.ASC);
        var sortedTable = command.execute();

        assertThat(sortedTable).isEqualTo(Table.of(List.of("id", "value"), List.of(
                List.of("1", "value1"),
                List.of("2", "value2"),
                List.of("3", "value3"),
                List.of("4", "value4")
        )));

    }

    @Test
    void shouldSortGivenTableDescending(){
        var table = Table.of(List.of("id", "value"), List.of(
                List.of("3", "value3"),
                List.of("4", "value4"),
                List.of("1", "value1"),
                List.of("2", "value2")
        ));

        var command = new OrderByCommand(table, "id", Sql.OrderBy.DESC);
        var sortedTable = command.execute();

        assertThat(sortedTable).isEqualTo(Table.of(List.of("id", "value"), List.of(
                List.of("4", "value4"),
                List.of("3", "value3"),
                List.of("2", "value2"),
                List.of("1", "value1")
        )));
    }

    @Test
    void shouldNotModifyInputTableOnSort(){
        var headers = List.of("id", "value");
        var rows = List.of(
                List.of("3", "value3"),
                List.of("4", "value4"),
                List.of("1", "value1"),
                List.of("2", "value2")
        );
        var table1 = Table.of(headers, rows);

        var command = new OrderByCommand(table1, "id", Sql.OrderBy.ASC);
        command.execute();

        assertThat(table1).isEqualTo(Table.of(headers, rows));

    }
}