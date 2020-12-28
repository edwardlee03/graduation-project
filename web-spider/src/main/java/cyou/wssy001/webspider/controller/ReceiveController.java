package cyou.wssy001.webspider.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cyou.wssy001.common.async.AsyncRequest;
import cyou.wssy001.common.dao.NovelInfoDao;
import cyou.wssy001.common.dao.UserInfoDao;
import cyou.wssy001.common.dao.UserNovelDao;
import cyou.wssy001.common.dto.SpiderMessageDTO;
import cyou.wssy001.common.dto.WechatMessageDTO;
import cyou.wssy001.common.entity.GlobalResult;
import cyou.wssy001.common.entity.NovelInfo;
import cyou.wssy001.common.entity.UserInfo;
import cyou.wssy001.common.entity.UserNovel;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @ProjectName: graduation-project
 * @ClassName: ReceiveController
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/12/7
 * @Version v1.0
 */
@RestController
@RequiredArgsConstructor
public class ReceiveController {
    private final UserInfoDao userInfoDao;
    private final NovelInfoDao novelInfoDao;
    private final UserNovelDao userNovelDao;
    private final AsyncRequest asyncRequest;

    @GetMapping("/test")
    public String test() {
        SpiderMessageDTO spiderMessageDTO = new SpiderMessageDTO();
        spiderMessageDTO.setNovelQdId(1);
        return new GlobalResult<>(200, "成功", spiderMessageDTO).toString();
    }

    @PostMapping("/receive")
    public GlobalResult<String> receive(
            @RequestBody GlobalResult<SpiderMessageDTO> result
    ) {
        SpiderMessageDTO data = result.getData();
        Integer novelQdId = data.getNovelQdId();
        String openId = data.getOpenId();
        NovelInfo novelInfo = getNovelById(novelQdId);

        StringBuilder stringBuilder = new StringBuilder();
        if (novelInfo == null) {
            stringBuilder.append("您查找的小说ID：").append(novelQdId).append(" 不存在，请检查！");
        } else {
            stringBuilder.append("\t书名\t\t作者\t最新章节\n");
            stringBuilder.append(novelInfo.getName())
                    .append("\t")
                    .append(novelInfo.getWriter())
                    .append("\t")
                    .append(novelInfo.getLatestChapter());
        }

        String from = data.getFrom();
        if (from.equals("wechat")) {
            WechatMessageDTO dto = new WechatMessageDTO();
            dto.setFrom("spider");
            dto.setOpenID(openId);
            dto.setMsg(stringBuilder.toString());
            asyncRequest.send(dto);
        }

        return new GlobalResult<>(200, "成功", null);
    }

    @PostMapping("/subscribe")
    public GlobalResult<String> subscribe(
            @RequestBody GlobalResult<SpiderMessageDTO> result
    ) {
        SpiderMessageDTO data = result.getData();
        Integer novelQdId = data.getNovelQdId();
        NovelInfo novelInfo = getNovelById(novelQdId);
        String openId = data.getOpenId();

        StringBuilder stringBuilder = new StringBuilder();
        if (novelInfo == null) {
            stringBuilder.append("您查找的小说ID：").append(novelQdId).append(" 不存在，请检查！");
        } else {
            UserInfo userInfo = userInfoDao.selectUserByOpenId(openId);
            UserNovel userNovel = userNovelDao.selectOne(
                    Wrappers.<UserNovel>lambdaQuery()
                            .eq(UserNovel::getUserId, userInfo.getId())
                            .eq(UserNovel::getNovelId, novelInfo.getId())
            );
            if (userNovel == null) {
                userNovelDao.insert(new UserNovel(userInfo.getId(), novelInfo.getId()));
                stringBuilder.append("订阅成功！");
            } else {
                stringBuilder.append("您已订阅该小说!");
            }
        }

        String from = data.getFrom();
        if (from.equals("wechat")) {
            WechatMessageDTO dto = new WechatMessageDTO();
            dto.setFrom("spider");
            dto.setOpenID(openId);
            dto.setMsg(stringBuilder.toString());
            asyncRequest.send(dto);
        }
        return new GlobalResult<>(200, "成功", null);
    }

    private NovelInfo getNovelById(Integer id) {
        NovelInfo novelInfo = novelInfoDao.selectOne(Wrappers.<NovelInfo>lambdaQuery().eq(NovelInfo::getNovelQdId, id));
        if (novelInfo != null) return novelInfo;

        String s = HttpRequest.get("https://book.qidian.com/info/" + id)
                .header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36")
                .execute()
                .body();
        if (s.contains("抱歉，页面无法访问")) return null;
        Document document = Jsoup.parse(s);
        Element element = document.getElementsByClass("book-info").get(0);
        String name = element.getElementsByTag("h1").get(0).getElementsByTag("em").get(0).text();
        return getNovelByName(name, id);
    }

    private NovelInfo getNovelByName(String name, Integer id) {
        String s = HttpRequest.get("https://www.qidian.com/search?kw=" + name)
                .header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36")
                .execute()
                .body();
        Document document = Jsoup.parse(s);
        Elements elementsByClass = document.getElementsByClass("res-book-item");
        NovelInfo novelInfo = new NovelInfo();
        elementsByClass.forEach(n -> {
            String bookID = n.attr("data-bid");

            if (bookID.equals(id.toString())) {
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
                } else {
                    novelInfo.setLastUpdateTime(DateUtil.parse(time, "yyyy-MM-dd"));
                }
                novelInfo.setNovelQdId(id);
                novelInfoDao.insert(novelInfo);
            }
        });
        return novelInfo;
    }
}
