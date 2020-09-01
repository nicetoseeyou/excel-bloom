package lab.nice.excel.bloom.editor;

import lab.nice.excel.bloom.conf.EditorConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GenericEditor implements Editor {

    private EditorConfig config;
    private String[] headers;
    private Pattern sourceFilterPattern;

    public GenericEditor(EditorConfig config) {
        this.config = config;
        this.headers = this.config.getTargetCsvHeader().split(this.config.getTargetDelimiter());
        this.sourceFilterPattern = Pattern.compile(this.config.getSourceFilterRegex());
    }

    @Override
    public boolean predicate(Row row) {
        if (row.getRowNum() > 0) {
            String sourceCellVal = cellVal(row.getCell(this.config.getSourceFilterIndex()));
            if (StringUtils.isNotBlank(sourceCellVal)) {
                Matcher matcher = sourceFilterPattern.matcher(sourceCellVal);
                return matcher.find();
            }
        }
        return false;
    }

    @Override
    public List<String> redact(List<String> filler) {
        String[] content = this.config.getTargetCsvTemplate().split(this.config.getTargetDelimiter());
        for (int i = 0, j = 0; i < content.length && j < filler.size(); i++) {
            if (content[i].equals(this.config.getTargetPlaceholder())) {
                content[i] = filler.get(j);
                j++;
            }
        }
        return Arrays.asList(content);
    }

    @Override
    public List<String> pick(Row row) {
        return this.config.getSourceIndexes().stream()
                .map(idx -> cellVal(row.getCell(idx)))
                .collect(Collectors.toList());
    }

    @Override
    public String[] headers() {
        return headers;
    }

    private String cellVal(Cell cell) {
        if (cell == null) {
            return null;
        } else {
            return cell.getRichStringCellValue().getString();
        }
    }
}
