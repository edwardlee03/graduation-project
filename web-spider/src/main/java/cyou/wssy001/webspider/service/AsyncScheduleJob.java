package cyou.wssy001.webspider.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cyou.wssy001.common.dao.LogInfoDao;
import cyou.wssy001.common.dao.NovelInfoDao;
import cyou.wssy001.common.entity.LogInfo;
import cyou.wssy001.common.entity.NovelInfo;
import cyou.wssy001.common.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @projectName: graduation-project
 * @className: AsyncScheduleJob
 * @description:
 * @author: alexpetertyler
 * @date: 2020/12/7
 * @Version: v1.0
 */
@Async
@RequiredArgsConstructor
public class AsyncScheduleJob {
    private final NovelInfoDao novelInfoDao;
    private final LogInfoDao logInfoDao;

    private boolean flag;

    @Scheduled(fixedDelay = 3600000)
    public void getHtml() {
        flag = false;
        List<NovelInfo> novelInfos = novelInfoDao.selectList(null);
        if (novelInfos == null) return;

        novelInfos.forEach(v -> {
            try {
                String s = HttpRequest.get("https://www.qidian.com/search?kw=" + v.getName())
                        .header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36")
                        .execute()
                        .body();
                FileWriter fileWriter = new FileWriter("src/main/resources" + v.getNovelQdId());
                fileWriter.write(s);
                Thread.sleep(1000 * 10);
            } catch (InterruptedException ignored) {

            }
        });

        flag = true;
    }

    @Scheduled(fixedDelay = 7200000)
    public void updateNovelInfo() {
        if (!flag) return;
        List<NovelInfo> novelInfos = novelInfoDao.selectList(null);
        if (novelInfos == null) return;
        novelInfos.forEach(v -> {
            String path = "src/main/resources" + v.getNovelQdId();

            if (FileUtil.isFile(new File(path))) {
                FileReader fileReader = new FileReader(path);
                String result = fileReader.readString();
                Document document = Jsoup.parse(result);
                Elements elementsByClass = document.getElementsByClass("res-book-item");
                elementsByClass.forEach(n -> {
                    NovelInfo novelInfo = new NovelInfo();
                    String bookID = n.attr("data-bid");

                    if (bookID.equals(v.getNovelQdId())) {
                        Elements elements = n.getElementsByClass("book-img-box").get(0).getElementsByTag("img").eq(0);
                        novelInfo.setPic("https:" + elements.attr("src"));
                        elements = n.getElementsByClass("book-mid-info");
                        String title = elements.get(0).getElementsByTag("a").eq(0).text();
                        novelInfo.setName(title);
                        String author = elements.get(0).getElementsByClass("author").get(0).getElementsByClass("name").text();
                        novelInfo.setWriter(author);
                        String description = elements.get(0).getElementsByClass("intro").text();
                        novelInfo.setDescription(description);
                        elements = elements.get(0).getElementsByClass("update");
                        String update = elements.get(0).getElementsByTag("a").text();

                        if (update.contains("最新更新")) {
                            update = update.substring(5);
                            novelInfo.setLatestChapter(update);
                        }
                        String time = elements.get(0).getElementsByTag("span").text();

                        if (time.contains("小时")) {
                            time = time.replace("小时前", "");
                            int i = Integer.parseInt(time);
                            novelInfo.setLastUpdateTime(DateUtil.offsetHour(new Date(), -i));
                        } else if (time.contains("分钟")) {
                            time = time.replace("分钟前", "");
                            int i = Integer.parseInt(time);
                            novelInfo.setLastUpdateTime(DateUtil.offsetMinute(new Date(), -i));
                        } else if (time.contains("昨日")) {
                            time = time.replace("昨日", "");
                            time = "2021-" + DateUtil.thisMonth() + "-" + DateUtil.yesterday() + " " + time + ":00";
                            novelInfo.setLastUpdateTime(DateUtil.parse(time));
                        } else {
                            novelInfo.setLastUpdateTime(DateUtil.parse(time, "yyyy-MM-dd"));
                        }
                        novelInfoDao.updateById(novelInfo);
                        log(novelInfo);
                    }
                });
            }
        });
    }

    private void log(NovelInfo novelInfo) {
        LogInfo logInfo = new LogInfo();
        logInfo.setClientName("spider");
        logInfo.setUuid("spider-" + IdUtil.fastSimpleUUID());
        logInfo.setMsg("小说：" + novelInfo.getNovelQdId() + " 更新完毕");
        logInfoDao.insert(logInfo);
        LogUtil.addLog(logInfo);
    }
}
