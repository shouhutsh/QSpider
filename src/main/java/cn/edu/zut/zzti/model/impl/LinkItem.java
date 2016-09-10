package cn.edu.zut.zzti.model.impl;

import cn.edu.zut.zzti.model.Item;
import cn.edu.zut.zzti.model.Resource;
import cn.edu.zut.zzti.model.TextResource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by shouhutsh on 16-9-4.
 */
public class LinkItem implements Item {

    private final String type;
    private final TextResource resource;
    private final Map<String, String> data;


    public LinkItem(TextResource resource, String type) {
        this.type = type;
        this.resource = resource;
        data = new LinkedHashMap<String, String>();
    }

    public String getType() {
        return type;
    }

    public String get(String key){
        return data.get(key);
    }

    public void put(String key, String value) {
        data.put(key, value);
    }

    public Resource getResource() {
        return resource;
    }
}
