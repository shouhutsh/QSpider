package cn.edu.zut.zzti.server;

import cn.edu.zut.zzti.core.Downloader;
import cn.edu.zut.zzti.model.impl.HtmlResource;
import cn.edu.zut.zzti.model.impl.HtmlTask;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

/**
 * Created by shouhutsh on 16-9-4.
 */
public class HtmlDownloader implements Downloader<HtmlTask, HtmlResource> {

    public HtmlResource download(HtmlTask task) {
        try {
            Request request = Request.Get(task.getUrl()).connectTimeout(task.getTimeout()).userAgent(task.getAgent());
            Content content = request.execute().returnContent();
            return new HtmlResource(task, content.asBytes(), task.getCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
