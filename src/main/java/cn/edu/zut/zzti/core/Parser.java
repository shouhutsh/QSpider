package cn.edu.zut.zzti.core;

import cn.edu.zut.zzti.model.Item;
import cn.edu.zut.zzti.model.Resource;

import java.util.List;

/**
 * Created by shouhutsh on 16-9-4.
 */
public interface Parser<R extends Resource, I extends Item> {

    List<I> parse(R resource);
}
