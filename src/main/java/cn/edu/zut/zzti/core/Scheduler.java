package cn.edu.zut.zzti.core;

import cn.edu.zut.zzti.model.Item;
import cn.edu.zut.zzti.model.Task;

import java.util.List;

/**
 * Created by shouhutsh on 16-9-4.
 */
public interface Scheduler<I extends Item, T extends Task> {

    List<I> run(T task);

    List<I> run(List<I> items);
}
