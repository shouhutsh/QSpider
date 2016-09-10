package cn.edu.zut.zzti.core;

import cn.edu.zut.zzti.model.Resource;
import cn.edu.zut.zzti.model.Task;

/**
 * Created by shouhutsh on 16-9-4.
 */
public interface Downloader<T extends Task, R extends Resource> {

    R download(T task);
}
