package lab.nice.excel.bloom;

import lab.nice.excel.bloom.conf.EditorConfig;
import lab.nice.excel.bloom.editor.Editor;
import lab.nice.excel.bloom.editor.GenericEditor;
import lab.nice.excel.bloom.util.FileHelper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class ExcelPicker {
    public static void main(String[] args) throws IOException {
        String file = "src\\main\\resources\\excel.picker.properties";
        Properties properties = FileHelper.readProperties(file);
        EditorConfig config = new EditorConfig(properties);
        Editor editor = new GenericEditor(config);

        try (InputStream inputStream = Files.newInputStream(Paths.get(config.getSourceFile()), StandardOpenOption.READ);
             XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
             BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(config.getTargetFile()),
                     StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
             CSVPrinter csvPrinter = CSVFormat.EXCEL.withHeader(editor.headers()).print(bufferedWriter)) {
            Sheet sheet = workbook.getSheetAt(config.getSourceSheetIndex());
            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (editor.predicate(row)) {
                    List<String> filler = editor.pick(row);
                    if (filler != null && !filler.isEmpty()) {
                        List<String> content = editor.redact(filler);
                        csvPrinter.printRecord(content);
                        csvPrinter.flush();
                    }
                }
            }
        }
    }
}
