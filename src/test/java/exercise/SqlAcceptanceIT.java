package exercise;

import exercise.parsers.CsvTableParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

}
