import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cyou.wssy001.common.entity.NovelInfo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class ProjectTest {
    @Test
    void test1() {
//        获取起点小说网中的查询结果页

//        String s = HttpRequest.get("https://www.qidian.com/search?kw=" + "沧元图")
//                .header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36")
//                .execute()
//                .body();
//        log.info("******\n" + s);

//        读取保存在本地的网页文本
        FileReader fileReader = new FileReader("D:\\workspace\\SpringCloud\\graduation-project\\spider-text.txt");
        String result = fileReader.readString();
//         将文本结果转换成对象
        Document document = Jsoup.parse(result);
//        获取书本信息“li”
        Elements elementsByClass = document.getElementsByClass("res-book-item");
//          定义一个存储“小说对象”的列表
        List<NovelInfo> novelInfos = new ArrayList<>();
//        对获取到的“li”进行遍历，相当于 for语句
        elementsByClass.forEach(v -> {
            NovelInfo novelInfo = new NovelInfo();
//            获取小说的ID
            String bookID = v.attr("data-bid");
//            获取小说封面的网址
            Elements elements = v.getElementsByClass("book-img-box").get(0).getElementsByTag("img").eq(0);
            novelInfo.setPic("https:" + elements.attr("src"));
//            获取小说的标题
            elements = v.getElementsByClass("book-mid-info");
            String title = elements.get(0).getElementsByTag("a").eq(0).text();
            novelInfo.setName(title);
//            获取小说的作者
            String author = elements.get(0).getElementsByClass("author").get(0).getElementsByClass("name").text();
            novelInfo.setWriter(author);
//            获取小说的简介
            String description = elements.get(0).getElementsByClass("intro").text();
            novelInfo.setDescription(description);
//            获取小说的最新更新章节
            elements = elements.get(0).getElementsByClass("update");
            String update = elements.get(0).getElementsByTag("a").text();
            if (update.contains("最新更新")) update = update.substring(5);
//            获取小说的更新时间
            String time = elements.get(0).getElementsByTag("span").text();
            if (time.contains("小时")) {
                time = time.replace("小时前", "");
                int i = Integer.parseInt(time);
                novelInfo.setLastUpdateTime(DateUtil.offsetHour(new Date(), -i));
            } else {
                novelInfo.setLastUpdateTime(DateUtil.parse(time, "yyyy-MM-dd"));
            }
//            将封装的“小说对象”存入列表
            novelInfos.add(novelInfo);
        });

    }

    @Test
    void test2() {
        String s = HttpRequest.get("https://book.qidian.com/info/1024051587")
                .header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36")
                .execute()
                .body();
//        if (s.contains("抱歉，页面无法访问"))
        Document document = Jsoup.parse(s);
        Element element = document.getElementsByClass("book-info").get(0);
        String name = element.getElementsByTag("h1").get(0).getElementsByTag("em").get(0).text();
        log.info("******" + name);
    }
}
