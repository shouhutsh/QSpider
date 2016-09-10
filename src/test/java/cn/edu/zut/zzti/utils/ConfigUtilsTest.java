package cn.edu.zut.zzti.utils;

/**
 * Created by ae-mp02 on 2016/9/9.
 */
public class ConfigUtilsTest {

    public static void main(String[] args) {
        final ConfigUtils configUtils = new ConfigUtils();
        for (String t : configUtils.getTasks()) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    System.out.println(configUtils.getLocalConfigUtils().getJSONObject());
                }
            });
            thread.setName(t);
            thread.start();
        }
    }
}
