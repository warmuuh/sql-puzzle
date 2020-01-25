package exercise.parsers;

import exercise.Table;
import exercise.exception.IllegalHeaderException;
import exercise.exception.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static exercise.utils.TestUtils.toStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CsvTableParserTest {

    @Test
    void shouldThrowExceptionOnNullParam() {
        var sut = new CsvTableParser();
        //i dont like javas decision to throw NPE on Objects.requireNotNull, but it is more idiomatic
        assertThatThrownBy(() -> sut.parseTable(null))
                    .isInstanceOf(NullPointerException.class);
    }


    @Test
    void shouldCloseStream() throws IOException {
        var sut = new CsvTableParser();
        var stream = spy(toStream(""));
        sut.parseTable(stream);
        verify(stream).close();
    }

    @Test
    void shouldCreateEmptyTable(){
        var sut = new CsvTableParser();
        var table = sut.parseTable(toStream(""));
        assertThat(table).isNotNull();
        assertThat(table.getHeaders()).isEmpty();
    }


    @Test
    void shouldReadSingleHeader(){
        var sut = new CsvTableParser();
        var table = sut.parseTable(toStream("header1"));
        assertThat(table).isNotNull();
        assertThat(table.getHeaders())
                .extracting(Table.Header::getName)
                .containsExactly("header1");
    }

    @Test
    void shouldReadMultipleHeader(){
        var sut = new CsvTableParser();
        var table = sut.parseTable(toStream("header1,header2"));
        assertThat(table).isNotNull();
        assertThat(table.getHeaders())
                .extracting(Table.Header::getName)
                .containsExactly("header1","header2");
    }

    @Test
    void shouldTrimParsedValues(){
        var sut = new CsvTableParser();
        var table = sut.parseTable(toStream("  header1  , header2  "));
        assertThat(table).isNotNull();
        assertThat(table.getHeaders())
                .extracting(Table.Header::getName)
                .containsExactly("header1","header2");
    }

    @Test
    void shouldRejectEmptyHeaders(){
        var sut = new CsvTableParser();
        assertThatThrownBy(() -> sut.parseTable(toStream("header1,,header2")))
                .isInstanceOf(IllegalHeaderException.class);
    }



    @Test
    void shouldParseValues(){
        String csvContent = "header1,header2\n"+
                            "value1,value2\n"+
                            "value3,value4\n";

        var sut = new CsvTableParser();
        var table = sut.parseTable(toStream(csvContent));
        assertThat(table).isNotNull();
        assertThat(table.getRows())
                .extracting(Table.Row::getValues)
                .containsExactly(List.of("value1", "value2"),
                                 List.of("value3", "value4"));

    }

    @Test
    void shouldHandleEmptyValues(){
        String csvContent = "header1,header2\n"+
                ",\n";

        var sut = new CsvTableParser();
        var table = sut.parseTable(toStream(csvContent));
        assertThat(table).isNotNull();
        assertThat(table.getRows())
                .extracting(Table.Row::getValues)
                .containsExactly(List.of("", ""));

    }


    @Test
    void throwParseExceptionOnError() throws IOException {
        var sut = new CsvTableParser();
        var stream = spy(toStream("test"));
        doThrow(new IOException("test")).when(stream).close();

        assertThatThrownBy(() -> sut.parseTable(stream))
                .isInstanceOf(ParseException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "header1,header2\nvalue1,value2,value3\n",
            "header1,header2\nvalue1\n"
    })
    void shouldHandleInvalidRows(String csvContent){
        var sut = new CsvTableParser(",", row -> {throw new IllegalArgumentException("test");});
        assertThatThrownBy(() -> sut.parseTable(toStream(csvContent)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("test");
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "header1,header2\nvalue1,value2,value3\n",
            "header1,header2\nvalue1\n"
    })
    void shouldIgnoreInvalidLinesByDefault(String csvContent){
        var sut = new CsvTableParser();
        assertThat(sut.parseTable(toStream(csvContent)).getRows()).isEmpty();
    }
}