package cyou.wssy001.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import cyou.wssy001.common.dao.NovelInfoDao;
import cyou.wssy001.common.entity.GlobalResult;
import cyou.wssy001.common.entity.NovelInfo;
import cyou.wssy001.common.vo.NovelInfoVo;
import cyou.wssy001.common.vo.PageVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ProjectName: graduation-project
 * @ClassName: NovelController
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/12/22
 * @Version v1.0
 */
@RestController
@RequestMapping("/novel")
@RequiredArgsConstructor
public class NovelController {
    private final NovelInfoDao novelInfoDao;

    @PostMapping("/")
    @ApiOperation("查询小说 - 分页")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码", example = "200", required = true, dataTypeClass = Integer.class),
            @DynamicParameter(name = "msg", value = "信息", example = "成功", required = true, dataTypeClass = String.class),
            @DynamicParameter(name = "data", value = "数据", required = true, dataTypeClass = PageVo.class)
    })
    public String test(
            @ApiParam(value = "额外的查询参数")
            @RequestParam(required = false) String query,
            @ApiParam(value = "当前页页码", required = true)
            @RequestParam Integer page,
            @ApiParam(value = "每页数据数量", required = true)
            @RequestParam Integer size
    ) {
        Page<NovelInfo> novelInfoPage = new Page<>();
        novelInfoPage.setCurrent(page);
        novelInfoPage.setSize(size);
        List<NovelInfo> novelInfos;
        if (query == null) {
            novelInfos = novelInfoDao.selectAllPage(novelInfoPage).getRecords();
        } else {
            novelInfos = novelInfoDao.selectByQuery(query, novelInfoPage).getRecords();
        }

        if (novelInfos == null) return new GlobalResult<String>(-200, "未找到相关数据", null).toString();

        List<NovelInfoVo> novelInfoVos = new ArrayList<>();
        novelInfos.forEach(v -> novelInfoVos.add(new NovelInfoVo(v.getId(), v.getName(), v.getWriter(), v.getDescription(), v.getNovelQdId(), v.getLatestChapter(), v.getEnable())));

        PageVo<NovelInfoVo> pageVo = new PageVo<>();
        pageVo.setCurrent(page);
        pageVo.setTotal(novelInfoPage.getTotal());
        pageVo.setData(novelInfoVos);
        GlobalResult<PageVo<NovelInfoVo>> result = new GlobalResult<>();
        result.setCode(200);
        result.setData(pageVo);
        result.setMsg("成功！");
        return result.toString();
    }

    @PostMapping("/delete")
    @ApiOperation("删除小说")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码", example = "200", required = true, dataTypeClass = Integer.class),
            @DynamicParameter(name = "msg", value = "信息", example = "成功", required = true, dataTypeClass = String.class)
    })
    public String testDelete(
            @ApiParam(value = "小说ID", required = true)
            @RequestParam Long id
    ) {
        novelInfoDao.deleteUserById(id);
        return new GlobalResult<String>(200, "成功", null).toString();
    }

    @PostMapping("/insert")
    @ApiOperation("测试增加")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码", example = "200", required = true, dataTypeClass = Integer.class),
            @DynamicParameter(name = "msg", value = "信息", example = "成功", required = true, dataTypeClass = String.class)
    })
    public String testInsert() {
        NovelInfo novelInfo = new NovelInfo();
        for (int i = 0; i < 10; i++) {
            novelInfo.setName("testName" + i);
            novelInfo.setWriter("testWriter" + i);
            novelInfo.setPic("testPicUrl" + i);
            novelInfo.setDescription("testDescription" + i);
            novelInfo.setBeginTime(new Date());
            novelInfo.setLastUpdateTime(novelInfo.getBeginTime());
            novelInfo.setNovelQdId(1000 + i);
            novelInfoDao.insert(novelInfo);
        }
        return new GlobalResult<String>(200, "成功", null).toString();
    }

    @PostMapping("/add")
    @ApiOperation("增加小说")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码", example = "200", required = true, dataTypeClass = Integer.class),
            @DynamicParameter(name = "msg", value = "信息", example = "成功", required = true, dataTypeClass = String.class)
    })
    public String testAdd(
            @ApiParam(value = "NovelInfoVo JSON", required = true)
            @RequestBody NovelInfoVo novelInfoVo
    ) {
        NovelInfo novelInfo = new NovelInfo();
        novelInfo.setName(novelInfoVo.getName());
        novelInfo.setWriter(novelInfoVo.getWriter());
        novelInfo.setPic("");
        novelInfo.setDescription(novelInfoVo.getDescription());
        novelInfo.setBeginTime(new Date());
        novelInfo.setNovelQdId(novelInfoVo.getNovelQdId());
        novelInfo.setLastUpdateTime(novelInfo.getBeginTime());
        novelInfo.setLatestChapter(novelInfoVo.getLatestChapter());
        int i = novelInfoDao.insert(novelInfo);
        if (i == 1) return new GlobalResult<String>(200, "成功", null).toString();
        return new GlobalResult<String>(-200, "失败", null).toString();
    }

    @PostMapping("/update")
    @ApiOperation("更新小说信息")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码", example = "200", required = true, dataTypeClass = Integer.class),
            @DynamicParameter(name = "msg", value = "信息", example = "成功", required = true, dataTypeClass = String.class)
    })
    public String testUpdate(
            @ApiParam(value = "NovelInfoVo JSON", required = true)
            @RequestBody NovelInfoVo novelInfoVo
    ) {
        if (novelInfoVo.getId() == null || novelInfoVo.getId() <= 0)
            return new GlobalResult<String>(-200, "小说ID必须为大于0的数！", null).toString();
        NovelInfo novelInfo = new NovelInfo();
        novelInfo.setId(novelInfoVo.getId());
        novelInfo.setName(novelInfoVo.getName());
        novelInfo.setWriter(novelInfoVo.getWriter());
        novelInfo.setDescription(novelInfoVo.getDescription());
        novelInfo.setLatestChapter(novelInfoVo.getLatestChapter());
        novelInfoDao.updateById(novelInfo);
        novelInfoDao.setUserEnable(novelInfoVo.getStatus(), novelInfoVo.getId());
        return new GlobalResult<String>(200, "成功", null).toString();
    }
}
