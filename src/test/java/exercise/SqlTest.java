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

}