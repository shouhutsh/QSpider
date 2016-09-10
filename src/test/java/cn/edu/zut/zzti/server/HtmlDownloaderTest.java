package cn.edu.zut.zzti.server;

import cn.edu.zut.zzti.model.TextResource;
import cn.edu.zut.zzti.model.impl.HtmlTask;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by ae-mp02 on 2016/9/7.
 */
public class HtmlDownloaderTest {

    private static String url;

    @Before
    public void setUp() throws Exception {
        url = "https://www.baidu.com";
    }

    @Test
    public void test(){
        HtmlDownloader downloader = new HtmlDownloader();
        TextResource resource = downloader.download(new HtmlTask(url));
        System.out.println(resource.asString());
    }
}
