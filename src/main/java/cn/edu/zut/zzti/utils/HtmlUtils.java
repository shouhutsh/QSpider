package cn.edu.zut.zzti.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ae-mp02 on 2016/9/8.
 */
public class HtmlUtils {

    private static final String EMPTY = "";
    public static String getResourceName(String path){
        String layouts[] = path.split("/");
        return layouts[layouts.length - 1];
    }

    // FIXME 简单做一下
    public static String getCompleteURL(String parentUrl, String curUrl) {
        if(curUrl.matches("^(http|ftp).*")) return curUrl;
        if(curUrl.startsWith("//")) return parentUrl.substring(0, parentUrl.indexOf("//")) + curUrl;
        if(curUrl.startsWith("/")) return getRoot(parentUrl) + curUrl;
        return getPath(parentUrl) + curUrl;
    }

    private static String getRoot(String url){
        return getRegex("^.*?://[^:/]+:?\\d*", url);
    }

    private static String getPath(String url){
        return getRegex("^.*?://[^:/]+:?\\d*.*/", url);
    }

    private static String getRegex(String regex, String str) {
        Matcher matcher = Pattern.compile(regex).matcher(str);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return EMPTY;
    }
}
