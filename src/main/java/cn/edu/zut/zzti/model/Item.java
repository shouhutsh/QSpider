package cn.edu.zut.zzti.model;

import java.io.Serializable;

/**
 * Created by shouhutsh on 16-9-4.
 */
public interface Item<R extends Resource> extends Serializable{

    R getResource();
}
