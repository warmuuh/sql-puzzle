package exercise.utils;

import lombok.experimental.UtilityClass;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public final class TestUtils {

    private TestUtils() {}

    public static InputStream toStream(String value){
        return new ByteArrayInputStream(value.getBytes());
    }
}
