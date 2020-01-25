package exercise;

import exercise.parsers.TableParser;
import org.junit.jupiter.api.Test;

import static exercise.utils.TestUtils.toStream;
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


}