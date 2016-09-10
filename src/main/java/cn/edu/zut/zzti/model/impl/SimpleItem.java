package cn.edu.zut.zzti.model.impl;

import cn.edu.zut.zzti.model.Item;
import cn.edu.zut.zzti.model.TextResource;

/**
 * Created by ae-mp02 on 2016/9/7.
 */
public class SimpleItem implements Item{

    private final String item;

    private final TextResource resource;

    public SimpleItem(TextResource resource, String item) {
        this.item = item;
        this.resource = resource;
    }

    public String getItem() {
        return item;
    }

    public TextResource getResource() {
        return resource;
    }
}
