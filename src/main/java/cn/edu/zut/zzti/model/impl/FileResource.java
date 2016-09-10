package cn.edu.zut.zzti.model.impl;

import cn.edu.zut.zzti.model.Task;
import cn.edu.zut.zzti.model.TextResource;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.nio.charset.Charset;

/**
 * Created by shouhutsh on 16-9-4.
 */
public class FileResource implements TextResource {

    private final FileTask task;

    private byte[] data;

    private String charset = "UTF-8";

    public FileResource(FileTask task) {
        this.task = task;
    }

    public FileResource(FileTask task, String charset) {
        this.task = task;
        this.charset = charset;
    }

    private void read(){
        try {
            int len;
            byte buffer[] = new byte[4096];
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(task.getUrl()));
            while((len = in.read(buffer)) != -1){
                stream.write(buffer, 0, len);
            }
            in.close();
            stream.close();
            data = stream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String asString() {
        return new String(getData(), Charset.forName(charset));
    }

    public Task getTask() {
        return task;
    }

    public byte[] getData() {
        if (null == data) {
            read();
        }
        return data;
    }
}
