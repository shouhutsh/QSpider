package cn.edu.zut.zzti.model.impl;

import cn.edu.zut.zzti.model.Task;

/**
 * Created by ae-mp02 on 2016/9/9.
 */
public class FileTask implements Task {
    private final String path;

    public FileTask(String path) {
        this.path = path;
    }

    public String getUrl() {
        return path;
    }
}
