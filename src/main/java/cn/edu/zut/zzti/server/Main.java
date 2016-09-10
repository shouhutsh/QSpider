package cn.edu.zut.zzti.server;

import cn.edu.zut.zzti.model.impl.HtmlTask;
import cn.edu.zut.zzti.model.impl.LinkItem;
import cn.edu.zut.zzti.utils.ConfigUtils;
import cn.edu.zut.zzti.utils.Constants;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by ae-mp02 on 2016/9/8.
 */
public class Main {

    public static void main(String[] args) {
        init();
        int task = 0;
        List<LinkItem> items= Collections.emptyList();
        ConfigUtils configUtils = new ConfigUtils();

        for (String t : configUtils.getTasks()) {
            Thread.currentThread().setName(t);
            if (++task == 1) {
                items = new HtmlScheduler().run(new HtmlTask(configUtils.getLocalConfigUtils().getConfigUtils(Constants.SCEDULER).getJSONObject().getString(Constants.URL)));
            } else {
                items = new HtmlScheduler().run(items);
            }
        }
    }

    private static void init(){
        File saveDir = new File(Constants.DEFAULT_DIR);
        if(!saveDir.exists()){
            saveDir.mkdirs();
        }
    }
}
