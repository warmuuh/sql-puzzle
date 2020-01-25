package exercise.parsers;

import exercise.Table;

import java.io.InputStream;

public interface TableParser {
    Table parseTable(InputStream csvContent);
}
