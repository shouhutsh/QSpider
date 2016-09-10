package cn.edu.zut.zzti.model.impl;

import cn.edu.zut.zzti.model.Task;
import cn.edu.zut.zzti.model.TextResource;

import java.nio.charset.Charset;

/**
 * Created by shouhutsh on 16-9-4.
 */
public class HtmlResource implements TextResource {

    private final byte[] data;

    private final HtmlTask task;

    private String charset = "UTF-8";

    public HtmlResource(HtmlTask task, byte[] data) {
        this.task = task;
        this.data = data;
    }

    public HtmlResource(HtmlTask task, byte[] data, String charset) {
        this.task = task;
        this.data = data;
        this.charset = charset;
    }

    public Task getTask() {
        return task;
    }

    public byte[] getData() {
        return data;
    }

    public String asString() {
        return new String(data, Charset.forName(charset));
    }
}
