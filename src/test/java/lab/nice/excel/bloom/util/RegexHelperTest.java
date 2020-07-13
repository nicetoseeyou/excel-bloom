package lab.nice.excel.bloom.util;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.junit.Assert;
import org.junit.Test;

public class RegexHelperTest {
    @Test
    public void testRemoveDuplicateSpace(){
        String spaces = "  a ) ABC  \nCD   EF  G    ";
        Assert.assertEquals("a ) ABC\nCD EF G", RegexHelper.removeDuplicateSpace(spaces));
    }

    @Test
    public void testRemoveEmptyLine(){
        String lines = "\n  \nabc\n  \n  \n!";
        System.out.println(RegexHelper.removeEmptyLine(lines));
        Assert.assertEquals("abc\n!", RegexHelper.removeEmptyLine(lines));
    }

    @Test
    public void testFindStr(){
        String text = "ab.) Guangzhou \n Meizhou \n c10d) Beijing \n ef";
        String regex = "(?m)(?:^\\s?\\w+\\.?\\s?\\))(.*|\\(.*\\))(.*|\\(.*\\))+";
        System.out.println(RegexHelper.find(text, regex, 1));
    }
}
