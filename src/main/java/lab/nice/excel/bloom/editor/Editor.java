package lab.nice.excel.bloom.editor;

import org.apache.poi.ss.usermodel.Row;

import java.util.List;

public interface Editor {
    boolean predicate(Row row);
    List<String> redact(List<String> filler);
    List<String> pick(Row row);
    String[] headers();
}
