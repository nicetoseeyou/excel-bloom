package lab.nice.excel.bloom;

import lab.nice.excel.bloom.util.RegexHelper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        String path = "src\\main\\resources";
        String inputExcel = "PCC.xlsx";
        String outputExcel = "PCC_Out.xlsx";

        int inputSheetIndex = 0;
        int inputProvinceIndex = 0;
        int inputCityIndex = 1;
        int inputCountyIndex = 2;
        String matchingRegex = "\\bGuangdong\\b";
        String headerRegex = "(?m)(?:^\\w+\\.?\\s?\\)\\s?)(.*|\\(.*\\))(.*|\\(.*\\))+";
        String delimiter = "↑";
        int headerGroup = 1;


        try (InputStream inputStream = Files.newInputStream(Paths.get(path, inputExcel), StandardOpenOption.READ);
             XSSFWorkbook inputWorkbook = new XSSFWorkbook(inputStream);
             OutputStream outputStream = Files.newOutputStream(Paths.get(path, outputExcel),
                     StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
             XSSFWorkbook outputWorkbook = new XSSFWorkbook()) {
            Sheet outputSheet = outputWorkbook.createSheet();
            int outputRowNum = 0;

            Sheet inputSheet = inputWorkbook.getSheetAt(inputSheetIndex);
            Iterator<Row> inputRowIterator = inputSheet.rowIterator();

            while (inputRowIterator.hasNext()) {
                Row inputRow = inputRowIterator.next();
                int inputRowNum = inputRow.getRowNum();
                if (inputRowNum == 0) {
                    Row outputRow = outputSheet.createRow(outputRowNum++);
                    addCell(outputRow, 0, "SourceRowNum");
                    addCell(outputRow, 1, "Province");
                    addCell(outputRow, 2, "City");
                    addCell(outputRow, 3, "County");
                    addCell(outputRow, 4, "Valid");
                } else {
                    Cell inputProvinceCell = inputRow.getCell(inputProvinceIndex);
                    if (inputProvinceCell != null) {
                        String inputProvinceVal = inputProvinceCell.getRichStringCellValue().getString();
                        String cleanedProvinceVal = cleanCellStr(inputProvinceVal);
                        if (matched(cleanedProvinceVal, matchingRegex)) {
                            Cell inputCityCell = inputRow.getCell(inputCityIndex);
                            Cell inputCountyCell = inputRow.getCell(inputCountyIndex);
                            if (inputCityCell != null && inputCountyCell != null) {
                                String inputCityVal = inputCityCell.getRichStringCellValue().getString();
                                String cleanedCityVal = cleanCellStr(inputCityVal);

                                String inputCountyVal = inputCountyCell.getRichStringCellValue().getString();
                                String cleanedCountyVal = cleanCellStr(inputCountyVal);
                                String groupedCountyVal = groupWithDelimiter(cleanedCountyVal, headerRegex, delimiter);

                                String[] provinceArr = cleanedProvinceVal.split("\n");
                                String[] cityArr = cleanedCityVal.split("\n");
                                String[] countyArr = groupedCountyVal.split("\n");

                                int provinceCount = provinceArr.length;
                                int cityCount = cityArr.length;
                                int countyCount = countyArr.length;

                                if (cityCount == countyCount) {
                                    if (cityCount > 1) {
                                        if (provinceCount > 1) {
                                            if (provinceCount == cityCount && allMatched(provinceArr, headerRegex)
                                                    && allMatched(cityArr, headerRegex) && allMatched(countyArr, headerRegex)) {
                                                for (int i = 0; i < provinceCount; i++) {
                                                    String province = extract(provinceArr[i], headerRegex, headerGroup);
                                                    if (matched(province, matchingRegex)) {
                                                        String city = extract(cityArr[i], headerRegex, headerGroup);
                                                        String[] counties = extract(countyArr[i], headerRegex, headerGroup).split(delimiter);
                                                        for (String county : counties) {
                                                            Row outputRow = outputSheet.createRow(outputRowNum++);
                                                            applyVals(outputRow, inputRowNum + 1, province, city, county, "YES");
                                                        }
                                                    }
                                                }
                                            } else {
                                                Row outputRow = outputSheet.createRow(outputRowNum++);
                                                applyVals(outputRow, inputRowNum + 1, cleanedProvinceVal, cleanedCityVal, cleanedCountyVal, "NO");
                                            }
                                        } else {
                                            if (matched(cleanedProvinceVal, headerRegex)) {
                                                Row outputRow = outputSheet.createRow(outputRowNum++);
                                                applyVals(outputRow, inputRowNum + 1, cleanedProvinceVal, cleanedCityVal, cleanedCountyVal, "NO");
                                            } else {
                                                if (allMatched(cityArr, headerRegex) && allMatched(countyArr, headerRegex)) {
                                                    for (int i = 0; i < cityCount; i++) {
                                                        String city = extract(cityArr[i], headerRegex, headerGroup);
                                                        String[] counties = extract(countyArr[i], headerRegex, headerGroup).split(delimiter);
                                                        for (String county : counties) {
                                                            Row outputRow = outputSheet.createRow(outputRowNum++);
                                                            applyVals(outputRow, inputRowNum + 1, cleanedProvinceVal, city, county, "YES");
                                                        }
                                                    }
                                                } else {
                                                    Row outputRow = outputSheet.createRow(outputRowNum++);
                                                    applyVals(outputRow, inputRowNum + 1, cleanedProvinceVal, cleanedCityVal, cleanedCountyVal, "NO");
                                                }
                                            }
                                        }
                                    } else {
                                        if (provinceCount > 1) {
                                            Row outputRow = outputSheet.createRow(outputRowNum++);
                                            applyVals(outputRow, inputRowNum + 1, cleanedProvinceVal, cleanedCityVal, cleanedCountyVal, "NO");
                                        } else {
                                            String province = extract(cleanedProvinceVal, headerRegex, headerGroup);
                                            String city = extract(cleanedCityVal, headerRegex, headerGroup);
                                            String[] counties = extract(cleanedCountyVal, headerRegex, headerGroup).split(delimiter);
                                            for (String county : counties) {
                                                Row outputRow = outputSheet.createRow(outputRowNum++);
                                                applyVals(outputRow, inputRowNum + 1, province, city, county, "YES");
                                            }
                                        }
                                    }
                                } else {
                                    Row outputRow = outputSheet.createRow(outputRowNum++);
                                    applyVals(outputRow, inputRowNum + 1, cleanedProvinceVal, cleanedCityVal, cleanedCountyVal, "NO");
                                }
                            } else {
                                Row outputRow = outputSheet.createRow(outputRowNum++);
                                if (inputCityCell == null && inputCountyCell == null) {
                                    applyVals(outputRow, inputRowNum + 1, cleanedProvinceVal, "", "", "NO");
                                } else if (inputCityCell != null) {
                                    applyVals(outputRow, inputRowNum + 1, cleanedProvinceVal,
                                            cleanCellStr(inputCityCell.getRichStringCellValue().getString())
                                            , "", "NO");
                                } else {
                                    applyVals(outputRow, inputRowNum + 1, cleanedProvinceVal, "",
                                            cleanCellStr(inputCountyCell.getRichStringCellValue().getString()),
                                            "NO");
                                }
                            }
                        }
                    }
                }
            }
            outputWorkbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Identify whether all the strings in the array are in the same pattern
     *
     * @param arr        the strings
     * @param matchRegex the pattern
     * @return true if all string in same pattern
     */
    private static boolean allMatched(String[] arr, String matchRegex) {
        boolean result = true;
        Pattern pattern = Pattern.compile(matchRegex);
        for (String str : arr) {
            result = pattern.matcher(str).find() && result;
        }
        return result;
    }

    /**
     * find string via regex expression and returning the specified matched group
     *
     * @param text       the text to be searched
     * @param regex      the regex expression
     * @param groupIndex the returning matched group index
     * @return the specified matched group text or null if not match
     */
    private static String findVal(String text, String regex, int groupIndex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(groupIndex);
        } else {
            return null;
        }
    }

    /**
     * find string via regex expression and returning the specified matched group
     *
     * @param text       the text to be searched
     * @param regex      the regex expression
     * @param groupIndex the returning matched group index
     * @return the specified matched group text or input text if not match
     */
    private static String extract(String text, String regex, int groupIndex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(groupIndex);
        } else {
            return text;
        }
    }

    private static void applyVals(Row row, int srcRowNum, String provinceVal,
                                  String cityVal, String countyVal, String valid) {
        addCell(row, 0, String.valueOf(srcRowNum));
        addCell(row, 1, provinceVal);
        addCell(row, 2, cityVal);
        addCell(row, 3, countyVal);
        addCell(row, 4, valid);
    }

    private static String groupWithDelimiter(String text, String headerRegex, String delimiter) {
        Pattern pattern = Pattern.compile(headerRegex);
        String[] arr = text.split("\n");
        int len = arr.length;
        if (len >= 2 && pattern.matcher(arr[0]).find()) {
            StringBuilder sb = new StringBuilder(arr[0]);
            for (int i = 1; i < len; i++) {
                if (pattern.matcher(arr[i]).find()) {
                    sb.append("\n");
                } else {
                    sb.append(delimiter);
                }
                sb.append(arr[i]);
            }
            return sb.toString();
        } else {
            return text;
        }
    }

    private static void addCell(Row row, int columnNum, String val) {
        Cell cell = row.createCell(columnNum);
        cell.setCellValue(val);
    }

    private static String cleanCellStr(String cellVal) {
        return RegexHelper.removeDuplicateSpace(RegexHelper.removeEmptyLine(cellVal)).trim();
    }

    private static boolean matched(String text, String matchRegex) {
        Pattern pattern = Pattern.compile(matchRegex);
        return pattern.matcher(text).find();
    }
}
