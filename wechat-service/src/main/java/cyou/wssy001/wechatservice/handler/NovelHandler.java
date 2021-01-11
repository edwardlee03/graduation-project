package cyou.wssy001.wechatservice.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cyou.wssy001.common.dao.NovelInfoDao;
import cyou.wssy001.common.dao.UserNovelDao;
import cyou.wssy001.common.entity.NovelInfo;
import cyou.wssy001.common.entity.UserInfo;
import cyou.wssy001.common.entity.UserNovel;
import cyou.wssy001.wechatservice.builder.NewsBuilder;
import cyou.wssy001.wechatservice.builder.TextBuilder;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSession;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @projectName: graduation-project
 * @className: NovelHandler
 * @description:
 * @author: alexpetertyler
 * @date: 2020/12/17
 * @Version: v1.0
 */
@Component
@RequiredArgsConstructor
public class NovelHandler extends AbstractHandler {
    private final UserNovelDao userNovelDao;
    private final NovelInfoDao novelInfoDao;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        String eventKey = wxMessage.getEventKey();
        UserInfo userInfo = (UserInfo) context.get("user");
        WxSession session = (WxSession) context.get("session");
        StringBuilder stringBuilder = new StringBuilder();

        flag:
        if (eventKey.equalsIgnoreCase("novel_latest")) {
            NovelInfo novelInfo = novelInfoDao.selectOne(Wrappers.<NovelInfo>lambdaQuery().orderByDesc(NovelInfo::getLastUpdateTime).last("limit 1"));
            if (novelInfo == null) {
                stringBuilder.append("小说库还没有数据，您来一本小说吧？");
                break flag;
            }

            WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
            List<WxMpXmlOutNewsMessage.Item> articles = new ArrayList<>();
            item.setDescription(novelInfo.getDescription());
            item.setPicUrl(novelInfo.getPic());
            item.setTitle(novelInfo.getName());
            item.setUrl("https://book.qidian.com/info/" + novelInfo.getNovelQdId());
            articles.add(item);

            return new NewsBuilder().build(articles, wxMessage, wxMpService);

        } else if (eventKey.equalsIgnoreCase("novel_mine")) {
            LambdaQueryWrapper<UserNovel> wrapper = Wrappers.<UserNovel>lambdaQuery().eq(UserNovel::getUserId, userInfo.getId());
            Integer integer = userNovelDao.selectCount(wrapper);
            if (integer == null || integer == 0) {
                stringBuilder.append("您还没有小说，快关注一本吧！");
                break flag;
            }

            List<Long> ids = userNovelDao.selectList(wrapper).stream().map(UserNovel::getNovelId).limit(5).collect(Collectors.toList());
            stringBuilder.append("\t书名\t\t作者\t最新章节\n");
            novelInfoDao.selectBatchIds(ids).forEach(v -> {
                stringBuilder.append(v.getName())
                        .append("\t")
                        .append(v.getWriter())
                        .append("\t")
                        .append(v.getLatestChapter())
                        .append("\n");
            });
            if (integer > 5) {
                integer -= 5;
                stringBuilder.append("还有：").append(integer).append(" 本小说未显示");
            }

        } else if (eventKey.equalsIgnoreCase("novel_subscribe")) {
            stringBuilder.append("请输入QD小说ID");
            session.setAttribute("lastOperation", "novel_subscribe");
        }

        if (stringBuilder.length() == 0) return null;
        return new TextBuilder().build(stringBuilder.toString(), wxMessage, wxMpService);
    }
}
