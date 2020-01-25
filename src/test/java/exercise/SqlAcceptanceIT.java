package exercise;

import exercise.parsers.CsvTableParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * contains tests directly derived from requirements in readme.md
 */
public class SqlAcceptanceIT {

    private Sql sql;

    @BeforeEach
    void setup(){
        sql = new Sql(new CsvTableParser());
    }

    @Test
    void shouldReadDataFromPurchases(){
        Table purchases = sql.readTable(getClass().getResourceAsStream("/purchases.csv"));
        assertThat(purchases.getHeaders()).hasSize(3);
        assertThat(purchases.getRows()).hasSize(8);
        assertThat(purchases.getHeaders())
                .extracting(Table.Header::getName)
                .containsExactly("AD_ID","TITLE","USER_ID");
    }

    @Test
    void shouldReadDataFromUsers(){
        Table users = sql.readTable(getClass().getResourceAsStream("/users.csv"));
        assertThat(users.getHeaders()).hasSize(3);
        assertThat(users.getRows()).hasSize(5);
        assertThat(users.getHeaders())
                .extracting(Table.Header::getName)
                .containsExactly("USER_ID","NAME","EMAIL");
    }

    @Test
    void shouldSupportOrderByDesc(){
        Table users = sql.readTable(getClass().getResourceAsStream("/users.csv"));
        var sortedTable = sql.orderBy(users, "USER_ID", Sql.OrderBy.DESC);

        assertThat(sortedTable.getRows())
                .extracting(row -> row.getValues().get(1))
                .containsExactly(  "paul", "lydia", "swen", "manuel", "andre");
    }

    @Test
    void shouldSupportInnerJoin(){
        Table users = sql.readTable(getClass().getResourceAsStream("/users.csv"));
        Table purchases = sql.readTable(getClass().getResourceAsStream("/purchases.csv"));
        var joined = sql.join(users, purchases, "USER_ID", "USER_ID");

        assertThat(joined).isEqualTo(Table.of(List.of("USER_ID", "NAME", "EMAIL", "AD_ID", "TITLE", "USER_ID"), List.of(
                List.of("2", "manuel", "manuel@foo.de", "4", "guitar-1" , "2"),
                List.of("1", "andre", "andre@bar.de", "1", "car-1" , "1"),
                List.of("1", "andre", "andre@bar.de", "2", "car-2" , "1"),
                List.of("1", "andre", "andre@bar.de", "3", "car-3" , "1"),
                List.of("1", "andre", "andre@bar.de", "9", "chair-1" , "1"),
                List.of("3", "swen", "swen@foo.de", "5", "guitar-2" , "3"),
                List.of("4", "lydia", "lydia@bar.de", "6", "table-2" , "4"),
                List.of("4", "lydia", "lydia@bar.de", "7", "table-1" , "4")
        )));
    }
}
