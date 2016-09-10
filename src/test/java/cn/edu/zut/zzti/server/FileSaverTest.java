package cn.edu.zut.zzti.server;

import cn.edu.zut.zzti.model.impl.FileResource;
import cn.edu.zut.zzti.model.impl.FileTask;
import cn.edu.zut.zzti.model.impl.HtmlTask;
import cn.edu.zut.zzti.utils.Constants;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by ae-mp02 on 2016/9/7.
 */
public class FileSaverTest {

    private static String url;

    private static String path;

    @Before
    public void setUp() throws Exception {
        path = Constants.DEFAULT_SAVE_FILE;
        url = "http://www.smzdm.com/jingxuan/";
    }

    @Test
    public void download() throws Exception {
        new FileSaver(path).saveResource(new HtmlDownloader().download(new HtmlTask(url)));
    }

    @Test
    public void parse() {
        System.out.println(new HtmlParser().parse(new FileResource(new FileTask(path))));
    }
}
