package cn.edu.zut.zzti.utils;

import cn.edu.zut.zzti.model.impl.FileResource;
import cn.edu.zut.zzti.model.impl.FileTask;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ae-mp02 on 2016/9/8.
 */
public class ConfigUtils {

    private JSONObject config;

    public ConfigUtils() {
    }

    public ConfigUtils(JSONObject config) {
        this.config = config;
    }

    public JSONObject getJSONObject() {
        if(null == config) {
            config = JSONObject.parseObject(new FileResource(new FileTask(Constants.DEFAULT_CONFIG)).asString());
        }
        return config;
    }

    public List<String> getTasks(){
        List<String> tasks = new ArrayList<String>();
        for(Object o : getJSONObject().getJSONArray(Constants.TASKS)){
            tasks.add(String.valueOf(o));
        }
        return tasks;
    }

    public ConfigUtils getLocalConfigUtils(){
        JSONObject localConfig = getJSONObject().getJSONObject(Thread.currentThread().getName());
        if(localConfig.containsKey(Constants.REF)) {
            JSONObject refConfig = getJSONObject().getJSONObject(localConfig.getString(Constants.REF));
            for (String k : localConfig.keySet()) {
                refConfig.put(k, localConfig.get(k));
            }
            localConfig = refConfig;
        }
        return new ConfigUtils(localConfig);
    }

    public ConfigUtils getConfigUtils(String name) {
        return new ConfigUtils(config.getJSONObject(name));
    }
}
