package exercise;

import exercise.exception.IllegalHeaderException;
import exercise.parsers.TableParser;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static exercise.utils.TestUtils.toStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SqlTest {

    @Test
    void shouldParseGivenStream(){
        var parser = mock(TableParser.class);
        var sut = new Sql(parser);
        var stream = toStream("test");
        sut.readTable(stream);
        verify(parser).parseTable(stream);
    }

    @Nested
    public class SqlOrderBy {

        @Test
        void shouldThrowOnInvalidColumnName(){
            var table = Table.of(List.of("id", "value"), List.of());

            var sql = new Sql(mock(TableParser.class));
            assertThatThrownBy(() -> sql.orderBy(table, "unknownColumn", Sql.OrderBy.ASC))
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

            var sql = new Sql(mock(TableParser.class));
            var sortedTable = sql.orderBy(table, "id", Sql.OrderBy.ASC);

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

            var sql = new Sql(mock(TableParser.class));
            var sortedTable = sql.orderBy(table, "id", Sql.OrderBy.DESC);

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

            var sql = new Sql(mock(TableParser.class));
            sql.orderBy(table1, "id", Sql.OrderBy.ASC);

            assertThat(table1).isEqualTo(Table.of(headers, rows));

        }
    }

    @Nested
    class SqlJoin {

        @Test
        void shouldThrowOnInvalidHeader() {
            var table1 = Table.of(List.of("id", "value"), List.of());
            var table2 = Table.of(List.of("ID", "VALUE"), List.of());

            var sql = new Sql(mock(TableParser.class));
            assertThatThrownBy(() -> sql.join(table1, table2,  "id", "unknown"))
                    .isInstanceOf(IllegalHeaderException.class);

            assertThatThrownBy(() -> sql.join(table1, table2,  "unknown", "id"))
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

            var sql = new Sql(mock(TableParser.class));
            var joinedTable = sql.join(table1, table2,  "id", "ID");

            assertThat(joinedTable).isEqualTo(Table.of(List.of("id", "value", "ID", "VALUE"), List.of(
                    List.of("1", "value1", "1", "VALUE1"))));
        }
    }

}