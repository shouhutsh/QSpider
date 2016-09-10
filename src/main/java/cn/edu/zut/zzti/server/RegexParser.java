package cn.edu.zut.zzti.server;

import cn.edu.zut.zzti.core.Parser;
import cn.edu.zut.zzti.model.TextResource;
import cn.edu.zut.zzti.model.impl.SimpleItem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ae-mp02 on 2016/9/7.
 */
public class RegexParser implements Parser<TextResource, SimpleItem> {

    public List<SimpleItem> parse(TextResource resource) {
        List<SimpleItem> items = new ArrayList<SimpleItem>();
        Pattern p = Pattern.compile("<a [^>]+>", Pattern.DOTALL);
        Matcher m = p.matcher(resource.asString());
        while (m.find()) {
            items.add(new SimpleItem(resource, m.group(0)));
        }
        return items;
    }
}
