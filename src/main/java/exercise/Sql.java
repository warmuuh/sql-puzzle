package exercise;

import exercise.parsers.TableParser;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;

@RequiredArgsConstructor
public class Sql {

    private final TableParser parser;

    public Table readTable(InputStream csvContent) {
        return parser.parseTable(csvContent);
    }	

    public Table orderByDesc(Table table, String columnName){
        return null;
    }

    public Table join(Table left, Table right, String joinColumnTableLeft, String joinColumnTableRight) { return null; }

}
