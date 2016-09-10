package cn.edu.zut.zzti.server;

import cn.edu.zut.zzti.core.Saver;
import cn.edu.zut.zzti.model.impl.HtmlResource;
import cn.edu.zut.zzti.model.impl.HtmlTask;
import cn.edu.zut.zzti.model.impl.LinkItem;
import cn.edu.zut.zzti.utils.ConfigUtils;
import cn.edu.zut.zzti.utils.Constants;
import cn.edu.zut.zzti.utils.HtmlUtils;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by shouhutsh on 2016/9/7.
 */
public class FileSaver implements Saver<HtmlResource, LinkItem> {

    private final String path;

    private ConfigUtils config = new ConfigUtils().getLocalConfigUtils().getConfigUtils(Constants.SAVER);

    public FileSaver(String path) {
        this.path = path;
    }

    public boolean saveResource(HtmlResource resource) {
        return saveResource(resource, false);
    }

    public boolean saveResource(HtmlResource resource, boolean append) {
        try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path, append));
            out.write(resource.getData());
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveItems(List<LinkItem> items) {
        try {
            if (config.getJSONObject().containsKey(Constants.ITEMS)) {
                JSONObject itemsFilter = config.getJSONObject().getJSONObject(Constants.ITEMS);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true)));
                for(LinkItem i : items) {
                    for (String type : itemsFilter.keySet()) {
                        if(type.equals(i.getType())) {
                            writer.write(i.getType());
                            for (Object o : itemsFilter.getJSONObject(type).getJSONArray(Constants.FIELDS)) {
                                writer.write("|" + i.get(String.valueOf(o)));
                            }
                            writer.write("\n");
                        }
                    }
                }
                writer.close();
            }
            if (config.getJSONObject().containsKey(Constants.TASKS)) {
                JSONObject itemsFilter = config.getJSONObject().getJSONObject(Constants.TASKS);
                for(LinkItem i : items) {
                    for (String type : itemsFilter.keySet()) {
                        if(type.equals(i.getType())) {
                            new FileSaver(getFileName(type, i, itemsFilter)).
                                    saveResource(new HtmlDownloader().
                                            download(new HtmlTask(
                                                    HtmlUtils.getCompleteURL(i.getResource().getTask().getUrl(), String.valueOf(i.get(itemsFilter.getJSONObject(type).getString(Constants.URL)))))));
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getFileName(String type, LinkItem i, JSONObject itemsFilter){
        return Constants.DEFAULT_DIR + HtmlUtils.getResourceName(String.valueOf(i.get(itemsFilter.getJSONObject(type).getString(Constants.URL))));
    }
}
