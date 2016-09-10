package cn.edu.zut.zzti.model.impl;

import cn.edu.zut.zzti.model.Task;

/**
 * Created by shouhutsh on 16-9-4.
 */
public class HtmlTask implements Task {

    private final String url;

    public HtmlTask(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public int getTimeout() {
        return 10000;
    }

    public String getAgent() {
        return "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11";
    }

    public String getCharset() {
        return "UTF-8";
    }
}
