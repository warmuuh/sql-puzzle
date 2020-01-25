package exercise;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    @Test
    void shouldCreateCorrectHeaderObjects(){
        var sut = Table.of(List.of("header1", "header2"), Collections.emptyList());
        assertThat(sut.getHeaders())
                .containsExactly(new Table.Header("header1", 0),
                                 new Table.Header("header2", 1));
    }

    @Test
    void shouldCreateCorrectRowObjects(){
        var sut = Table.of(List.of("header1", "header2"), List.of(List.of("row1Value1", "row1Value2"), List.of("row2Value1", "row2Value2")));
        assertThat(sut.getRows())
                .containsExactly(new Table.Row(List.of("row1Value1", "row1Value2")),
                                 new Table.Row(List.of("row2Value1", "row2Value2")));
    }

    @Test
    void shouldSupportEquals(){
        var table1 = Table.of(List.of("header1", "header2"), Collections.emptyList());
        var table2 = Table.of(List.of("header1", "header2"), Collections.emptyList());
        assertThat(table1).isEqualTo(table2);
    }

    @Test
    void shouldSupportHashcode(){
        var table1 = Table.of(List.of("header1", "header2"), Collections.emptyList());
        var table2 = Table.of(List.of("header1", "header2"), Collections.emptyList());
        assertThat(table1.hashCode()).isEqualTo(table2.hashCode());
    }

    @Test
    void shouldLookupHeaders(){
        var sut = Table.of(List.of("header1", "header2"), Collections.emptyList());

        assertThat(sut.getHeader("header1")).contains(new Table.Header("header1", 0));
        assertThat(sut.getHeader("unknownHeader")).isEmpty();
    }
}