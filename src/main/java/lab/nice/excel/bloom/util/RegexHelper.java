package lab.nice.excel.bloom.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexHelper {
    private RegexHelper() {
    }

    private static final String REGEX_BLANK_LINE = "(?m)^\\s*$\r?\n";
    private static final String REGEX_DUPLICATE_SPACE = "(?m)^ +| +$|( )+";

    public static String removeEmptyLine(String lines) {
        return lines.replaceAll(REGEX_BLANK_LINE, "");
    }

    public static String removeDuplicateSpace(String text) {
        return text.replaceAll(REGEX_DUPLICATE_SPACE, "$1");
    }

    public static List<String> find(String text, String regex, int groupIndex){
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()){
            list.add(matcher.group(groupIndex));
        }
        return list;
    }
}
