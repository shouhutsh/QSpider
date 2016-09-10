package cn.edu.zut.zzti.model;

import java.io.Serializable;

/**
 * Created by shouhutsh on 16-9-4.
 */
public interface Resource<T extends Task> extends Serializable {

    T getTask();

    byte[] getData();
}
