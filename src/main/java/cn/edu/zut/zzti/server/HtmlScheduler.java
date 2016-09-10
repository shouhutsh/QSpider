package cn.edu.zut.zzti.server;

import cn.edu.zut.zzti.core.Scheduler;
import cn.edu.zut.zzti.model.impl.HtmlTask;
import cn.edu.zut.zzti.model.impl.LinkItem;
import cn.edu.zut.zzti.utils.ConfigUtils;
import cn.edu.zut.zzti.utils.Constants;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ae-mp02 on 2016/9/8.
 */
public class HtmlScheduler implements Scheduler<LinkItem, HtmlTask> {

    private int depth = Integer.MAX_VALUE;
    private ConfigUtils config;

    public HtmlScheduler() {
        config = new ConfigUtils().getLocalConfigUtils().getConfigUtils(Constants.SCEDULER);
        if (config.getJSONObject().containsKey(Constants.DEPTH)) {
            depth = config.getJSONObject().getInteger(Constants.DEPTH);
        }
    }

    public List<LinkItem> run(HtmlTask task) {
        if(--depth < 0) return Collections.emptyList();
        return run(new HtmlParser().parse(new HtmlDownloader().download(task)));
    }

    public List<LinkItem> run(List<LinkItem> items) {
        List<LinkItem> allItems = new ArrayList<LinkItem>(items);
        if (config.getJSONObject().containsKey(Constants.TASKS)) {
            JSONObject itemsFilter = config.getJSONObject().getJSONObject(Constants.TASKS);
            for (LinkItem i : items) {
                for (String type : itemsFilter.keySet()) {
                    if (type.equals(i.getType())) {
                        allItems.addAll(run(new HtmlTask(String.valueOf(i.get(itemsFilter.getJSONObject(type).getString(Constants.URL))))));
                    }
                }
            }
        }
        new FileSaver(Constants.DEFAULT_SAVE_FILE).saveItems(items);
        return allItems;
    }
}
