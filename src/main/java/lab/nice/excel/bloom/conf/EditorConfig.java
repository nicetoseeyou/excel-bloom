package lab.nice.excel.bloom.conf;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public class EditorConfig {
    private final String sourceFile;
    private final String targetFile;
    private final Integer sourceSheetIndex;
    private final List<Integer> sourceIndexes;
    private final int sourceFilterIndex;
    private final String sourceFilterRegex;
    private final String targetDelimiter;
    private final String targetPlaceholder;
    private final String targetCsvHeader;
    private final String targetCsvTemplate;

    public EditorConfig(Properties conf) {
        this.sourceFile = conf.getProperty("src.file");
        this.targetFile = conf.getProperty("tgt.file");
        this.sourceSheetIndex = Integer.parseInt(conf.getProperty("src.sheet.index"));
        this.sourceIndexes = Arrays.stream(conf.getProperty("src.indexes").split(","))
                .filter(StringUtils::isNotBlank)
                .map(Integer::parseInt).collect(Collectors.toList());
        this.sourceFilterIndex = Integer.parseInt(conf.getProperty("src.filter.index"));
        this.sourceFilterRegex = conf.getProperty("src.filter.regex", ".*");
        this.targetDelimiter = conf.getProperty("tgt.delimiter");
        this.targetPlaceholder = conf.getProperty("tgt.placeholder");
        this.targetCsvHeader = conf.getProperty("tgt.csv.header");
        this.targetCsvTemplate = conf.getProperty("tgt.csv.template");
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public String getTargetFile() {
        return targetFile;
    }

    public Integer getSourceSheetIndex() {
        return sourceSheetIndex;
    }

    public List<Integer> getSourceIndexes() {
        return sourceIndexes;
    }

    public int getSourceFilterIndex() {
        return sourceFilterIndex;
    }

    public String getSourceFilterRegex() {
        return sourceFilterRegex;
    }

    public String getTargetDelimiter() {
        return targetDelimiter;
    }

    public String getTargetPlaceholder() {
        return targetPlaceholder;
    }

    public String getTargetCsvHeader() {
        return targetCsvHeader;
    }

    public String getTargetCsvTemplate() {
        return targetCsvTemplate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EditorConfig config = (EditorConfig) o;
        return sourceFilterIndex == config.sourceFilterIndex &&
                Objects.equals(sourceFile, config.sourceFile) &&
                Objects.equals(targetFile, config.targetFile) &&
                Objects.equals(sourceSheetIndex, config.sourceSheetIndex) &&
                Objects.equals(sourceIndexes, config.sourceIndexes) &&
                Objects.equals(sourceFilterRegex, config.sourceFilterRegex) &&
                Objects.equals(targetDelimiter, config.targetDelimiter) &&
                Objects.equals(targetPlaceholder, config.targetPlaceholder) &&
                Objects.equals(targetCsvHeader, config.targetCsvHeader) &&
                Objects.equals(targetCsvTemplate, config.targetCsvTemplate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceFile, targetFile, sourceSheetIndex, sourceIndexes, sourceFilterIndex, sourceFilterRegex, targetDelimiter, targetPlaceholder, targetCsvHeader, targetCsvTemplate);
    }

    @Override
    public String toString() {
        return "EditorConfig{" +
                "sourceFile='" + sourceFile + '\'' +
                ", targetFile='" + targetFile + '\'' +
                ", sourceSheetIndex=" + sourceSheetIndex +
                ", sourceIndexes=" + sourceIndexes +
                ", sourceFilterIndex=" + sourceFilterIndex +
                ", sourceFilterRegex='" + sourceFilterRegex + '\'' +
                ", targetDelimiter='" + targetDelimiter + '\'' +
                ", targetPlaceholder='" + targetPlaceholder + '\'' +
                ", targetCsvHeader='" + targetCsvHeader + '\'' +
                ", targetCsvTemplate='" + targetCsvTemplate + '\'' +
                '}';
    }
}
