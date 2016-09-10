package cn.edu.zut.zzti.server;

import cn.edu.zut.zzti.core.Parser;
import cn.edu.zut.zzti.model.TextResource;
import cn.edu.zut.zzti.model.impl.LinkItem;
import cn.edu.zut.zzti.utils.ConfigUtils;
import cn.edu.zut.zzti.utils.Constants;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by shouhutsh on 16-9-4.
 */
public class HtmlParser implements Parser<TextResource, LinkItem> {

    public List<LinkItem> parse(TextResource resource) {
        List<LinkItem> items = new ArrayList<LinkItem>();
        Elements all = Jsoup.parse(resource.asString()).children();

        JSONObject parser = new ConfigUtils().getLocalConfigUtils().getConfigUtils(Constants.PARSER).getJSONObject();
        for (String key : parser.keySet()) {
            Elements select = all;
            JSONObject kind = parser.getJSONObject(key);

            for (Object s : kind.getJSONArray(Constants.SELECT)) {
                select = select.select(String.valueOf(s));
            }
            for(Element e : select){
                if(kind.containsKey(Constants.REGEX) && !Pattern.matches(kind.getString(Constants.REGEX), String.valueOf(e))){
                    continue;
                }
                LinkItem item = new LinkItem(resource, key);
                for(Map.Entry a : kind.getJSONObject(Constants.GET).entrySet()) {
                    if (Constants.ATTR.equals(a.getKey())) {
                        item.put(String.valueOf(a.getValue()), e.attr(String.valueOf(a.getValue())));
                    } else if (Constants.TEXT.equals(a.getKey())) {
                        item.put(String.valueOf(a.getValue()), e.text());
                    }
                }
                items.add(item);
            }
        }
        return items;
    }
}
