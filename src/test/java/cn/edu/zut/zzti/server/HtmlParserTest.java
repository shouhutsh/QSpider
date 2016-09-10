package cn.edu.zut.zzti.server;

import cn.edu.zut.zzti.model.Item;
import cn.edu.zut.zzti.model.impl.FileResource;
import cn.edu.zut.zzti.model.impl.FileTask;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by ae-mp02 on 2016/9/7.
 */
public class HtmlParserTest {

    private static String path;

    @Before
    public void setUp() throws Exception {
        path = "/home/shouhutsh/Downloads/index.html";
    }

    @Test
    public void test(){
        FileResource resource = new FileResource(new FileTask(path));
        for (Item i : new HtmlParser().parse(resource)) {
            System.out.println(i);
        }
    }
}
