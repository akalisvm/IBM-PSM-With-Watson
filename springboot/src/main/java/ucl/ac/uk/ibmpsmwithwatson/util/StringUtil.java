package ucl.ac.uk.ibmpsmwithwatson.util;

public class StringUtil {
    public static String firstUpperCase(String s) {
        s = s.trim();
        int len = s.length();
        if(len == 1) {
            return s.toUpperCase();
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1, len).toLowerCase();
    }
}
