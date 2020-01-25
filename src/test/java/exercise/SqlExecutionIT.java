package exercise;

import exercise.parsers.CsvTableParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SqlExecutionIT {

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
        Table purchases = sql.readTable(getClass().getResourceAsStream("/users.csv"));
        assertThat(purchases.getHeaders()).hasSize(3);
        assertThat(purchases.getRows()).hasSize(5);
    }
}
