package cn.edu.zut.zzti.utils;

import cn.edu.zut.zzti.model.impl.HtmlResource;
import cn.edu.zut.zzti.model.impl.HtmlTask;
import cn.edu.zut.zzti.server.HtmlDownloader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Created by shouhutsh on 16-11-12.
 */
public class Metro58UtilsTest {

    BufferedReader reader;

    @Before
    public void setUp() throws IOException {
        reader = new BufferedReader(new FileReader("/home/shouhutsh/code/git/QSpider/content/save.list"));
    }

    @After
    public void end() throws IOException {
        reader.close();
    }

    private static final String MetroName = "_metro_name_";
    private static final String MetroLink = "_metro_link_";

    private static final String SplitPoint = "整条线路";

    @Test
    public void parse() throws IOException {
        List<String> metroName = new ArrayList<String>();
        List<String> metroLink = new ArrayList<String>();

        String line;
        while (null != (line = reader.readLine())){
            String kv[] = line.split("\\|");
            if (MetroName.equals(kv[0])) {
                metroName.add(kv[1]);
            } else {
                metroLink.add(kv[1]);
            }
        }
        Map<String, Station> stationMap = setData(metroName, metroLink);
        Map<String, List<Double>> sampleMap = getSamples(stationMap.values());
        List<BreadthFirstFunction> firstFunctions = getFunctions(sampleMap);
        breadthFirst("徐家汇站", stationMap, firstFunctions);
        breadthFirst("漕河泾开发区站", stationMap, firstFunctions);
    }

    private void breadthFirst(String start, final Map<String, Station> stations, List<BreadthFirstFunction> functions) {
        System.out.println("开始广度遍历" + start);
        doBreadthFirst(new HashSet<Station>(Arrays.asList(stations.get(start))), new HashSet<Station>(), 0, functions);
        System.out.println("遍历结束!");
    }

    private void doBreadthFirst(Set<Station> waitStations, Set<Station> route, int deepth, List<BreadthFirstFunction> functions){
        if(waitStations.isEmpty()) return;
        Set<Station> temp = new HashSet<Station>();
        for (Station s : delete(waitStations, route)) {
            temp.addAll(delete(s.allNextStations(), route));
            route.add(s);
            for (BreadthFirstFunction f : functions) {
                f.doSomething(deepth, s);
            }
        }
        doBreadthFirst(temp, route, ++deepth, functions);
    }

    private Set<Station> delete(Set<Station> allNextStations, Set<Station> route) {
        Set<Station> temp = new HashSet<Station>();
        for (Station s : allNextStations) {
            if(route.contains(s)) continue;
            temp.add(s);
        }
        return temp;
    }

    private Map<String, List<Double>> getSamples(Collection<Station> stations) {
        Map<String, List<Double>> samples = new HashMap<String, List<Double>>();
        for (Station s : stations) {
            System.out.println("正在获取样本：" + s.name);
            samples.put(s.name, allSamplesForStation(10, String.format("http://sh.58.com%sj2/", s.link)));
        }
        return samples;
    }

    private List<Double> allSamplesForStation(int deepth, String url) {
        List<Double> samples = new ArrayList<Double>();
        if (null == url || 0 == url.length() || 0 == deepth) {
            return samples;
        }
        HtmlResource resource = null;
        while (null == resource) {
            resource = new HtmlDownloader().download(new HtmlTask(url));
            if (null == resource) {
                try { Thread.sleep(100); } catch (InterruptedException e) {}
            }
        }
        Elements all = Jsoup.parse(resource.asString()).children();
        Elements select = all.select("div.content").select("ul.listUl").select("div.money").select("b");
        for(Element e : select){
            try {
                samples.add(Double.valueOf(e.text()));
            } catch (Exception e1) {
                // ignore
            }
        }
        samples.addAll(allSamplesForStation(--deepth, getNextPageUrl(all)));
        return samples;
    }

    private String getNextPageUrl(Elements all) {
        for (Element a : all.select("div.pager").select("a.next")) {
            return getComplateUrl(a.attr("href"));
        }
        return null;
    }

    private String getComplateUrl(String link){
        return String.format("http://sh.58.com%s", link);
    }

    private List<BreadthFirstFunction> getFunctions(final Map<String, List<Double>> sampleMap){
        List<BreadthFirstFunction> functions = new ArrayList<BreadthFirstFunction>();
        functions.add(new BreadthFirstFunction() {
            public void doSomething(int deepth, Station station) {
                System.out.print(String.format(" | Deep: %d, Name: %s, Count: %d", deepth, station.name, sampleMap.get(station.name).size()));
            }
        });
        functions.add(new BreadthFirstFunction() {
            public void doSomething(int deepth, Station station) {
                System.out.print(String.format(" | AVE: %.2f", ave(sampleMap.get(station.name))));
            }
        });
        functions.add(new BreadthFirstFunction() {
            public void doSomething(int deepth, Station station) {
                int step = 500;
                System.out.print(String.format(" | Step: %d, Distribution: %s", step, distribution(step, sampleMap.get(station.name))));
            }
        });
        functions.add(new BreadthFirstFunction() {
            public void doSomething(int deepth, Station station) {
                System.out.println(" | ");
            }
        });
        return functions;
    }

    private Map<String, Station> setData(List<String> metroName, List<String> metroLink) {
        Map<String, Station> stations = new HashMap<String, Station>();
        Station prev = null;
        for(int i = 0; i < metroName.size(); ++i) {
            if (SplitPoint.equals(metroName.get(i))) {
                ++i;
                prev = getStation(metroName.get(i), metroLink.get(i), stations);
            } else {
                Station next = getStation(metroName.get(i), metroLink.get(i), stations);
                Step step = new Step(prev, next);
                prev.addStep(step);
                next.addStep(step);
                prev = next;
            }
        }
        Station yishan = stations.get("宜山路");
        Station shanghai = stations.get("上海体育馆站");
        Step step = new Step(yishan, shanghai);
        yishan.addStep(step);
        shanghai.addStep(step);
        return stations;
    }

    private Station getStation(String name, String link, Map<String, Station> cache) {
        Station s = cache.get(name);
        if (null == s) {
            s = new Station(name, link);
            cache.put(name, s);
        }
        return s;
    }

    private double ave(List<Double> doubles) {
        double total = 0;
        for (Double d : doubles) {
            total += d;
        }
        return total / doubles.size();
    }

    private Map<Integer, Integer> distribution(int step, List<Double> doubles) {
        Map<Integer, Integer> map = new TreeMap<Integer, Integer>();
        for (Double d : doubles) {
            Integer k = ((int)(d / step + 1)) * step;
            Integer v = map.get(k);
            if (null == v) {
                v = new Integer(0);
            }
            map.put(k, ++v);
        }
        return map;
    }

    private class Station implements Serializable {
        public final String name;
        public final String link;

        private Set<Step> steps;

        public Station(String name, String link) {
            this.name = name;
            this.link = link;
            steps = new TreeSet<Step>(new MyComparator());
        }

        public void addStep(Step step) {
            steps.add(step);
        }

        public Set<Step> getSteps() {
            return steps;
        }

        public Set<Station> allNextStations() {
            Set stations = new HashSet();
            for (Step s : steps) {
                if (this == s.prev) {
                    stations.add(s.next);
                } else if (this == s.next) {
                    stations.add(s.prev);
                }
            }
            return stations;
        }

        @Override
        public String toString() {
            return String.format("{\"%s\"}", name, link);
        }
    }

    private class Step implements Serializable {
        public final Station prev;
        public final Station next;

        public Step(Station prev, Station next) {
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            return String.format(" %s <=> %s", prev, next);
        }
    }

    private class MyComparator implements Comparator<Step> {
        public int compare(Step o1, Step o2) {
            if (o1.prev == o2.prev && o1.next == o2.next) {
                return 0;
            }
            if (o1.next == o2.prev && o1.prev == o2.next) {
                return 0;
            }
            return -1;
        }
    }

    private interface BreadthFirstFunction{
        void doSomething(int deepth, Station station);
    }
}
