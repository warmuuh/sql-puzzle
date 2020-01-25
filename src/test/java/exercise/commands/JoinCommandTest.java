package exercise.commands;

import exercise.Sql;
import exercise.Table;
import exercise.exception.IllegalHeaderException;
import exercise.parsers.TableParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class JoinCommandTest {

    @Test
    void shouldThrowOnInvalidHeader() {
        var table1 = Table.of(List.of("id", "value"), List.of());
        var table2 = Table.of(List.of("ID", "VALUE"), List.of());

        assertThatThrownBy(() -> new JoinCommand(table1, table2, "id", "unknown").execute())
                .isInstanceOf(IllegalHeaderException.class);

        assertThatThrownBy(() -> new JoinCommand(table1, table2, "unknown", "ID").execute())
                .isInstanceOf(IllegalHeaderException.class);
    }

    @Test
    void shouldJoinFoundValues() {
        var table1 = Table.of(List.of("id", "value"), List.of(
                List.of("1", "value1"),
                List.of("2", "value2")));
        var table2 = Table.of(List.of("ID", "VALUE"), List.of(
                List.of("1", "VALUE1"),
                List.of("3", "VALUE3")));

        var command = new JoinCommand(table1, table2,  "id", "ID");
        var joinedTable = command.execute();

        assertThat(joinedTable).isEqualTo(Table.of(List.of("id", "value", "ID", "VALUE"), List.of(
                List.of("1", "value1", "1", "VALUE1"))));
    }

    @Test
    void shouldJoinMultipleFoundValues() {
        var table1 = Table.of(List.of("id", "value"), List.of(
                List.of("1", "value1"),
                List.of("2", "value2")));
        var table2 = Table.of(List.of("ID", "VALUE"), List.of(
                List.of("1", "VALUE1"),
                List.of("3", "VALUE3"),
                List.of("1", "VALUE4")));

        var command = new JoinCommand(table1, table2,  "id", "ID");
        var joinedTable = command.execute();

        assertThat(joinedTable).isEqualTo(Table.of(List.of("id", "value", "ID", "VALUE"), List.of(
                List.of("1", "value1", "1", "VALUE1"),
                List.of("1", "value1", "1", "VALUE4")
        )));
    }



    @Test
    void shouldJoinEmptyLeft() {
        var table1 = Table.of(List.of("id", "value"), List.of());
        var table2 = Table.of(List.of("ID", "VALUE"), List.of(
                List.of("1", "VALUE1"),
                List.of("3", "VALUE3"),
                List.of("1", "VALUE4")));

        var command = new JoinCommand(table1, table2,  "id", "ID");
        var joinedTable = command.execute();

        assertThat(joinedTable).isEqualTo(Table.of(List.of("id", "value", "ID", "VALUE"), List.of()));
    }

    @Test
    void shouldJoinEmptyRight() {

        var table1 = Table.of(List.of("id", "value"), List.of(
                List.of("1", "VALUE1"),
                List.of("3", "VALUE3"),
                List.of("1", "VALUE4")));
        var table2 = Table.of(List.of("ID", "VALUE"), List.of());

        var command = new JoinCommand(table1, table2,  "id", "ID");
        var joinedTable = command.execute();

        assertThat(joinedTable).isEqualTo(Table.of(List.of("id", "value", "ID", "VALUE"), List.of()));
    }
}