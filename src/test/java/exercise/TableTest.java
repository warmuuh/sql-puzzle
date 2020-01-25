package exercise;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    @Test
    void shouldCreateCorrectHeaderObjects(){
        var sut = new Table(List.of("header1", "header2"), Collections.emptyList());
        assertThat(sut.getHeaders())
                .containsExactly(new Table.Header("header1", 0),
                                 new Table.Header("header2", 1));
    }

    @Test
    void shouldCreateCorrectRowObjects(){
        var sut = new Table(List.of("header1", "header2"), List.of(List.of("row1Value1", "row1Value2"), List.of("row2Value1", "row2Value2")));
        assertThat(sut.getRows())
                .containsExactly(new Table.Row(List.of("row1Value1", "row1Value2"), 0),
                                 new Table.Row(List.of("row2Value1", "row2Value2"), 1));
    }

}