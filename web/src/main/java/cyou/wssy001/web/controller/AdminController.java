package cyou.wssy001.web.controller;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import cyou.wssy001.common.dao.AdminInfoDao;
import cyou.wssy001.common.entity.AdminInfo;
import cyou.wssy001.common.entity.GlobalResult;
import cyou.wssy001.common.vo.AdminInfoVo;
import cyou.wssy001.common.vo.PageVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: graduation-project
 * @ClassName: AdminController
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/12/22
 * @Version v1.0
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminInfoDao adminInfoDao;

    @PostMapping("/")
    @ApiOperation("查询管理员 - 分页")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码", example = "200", required = true, dataTypeClass = Integer.class),
            @DynamicParameter(name = "msg", value = "信息", example = "成功", required = true, dataTypeClass = String.class),
            @DynamicParameter(name = "data", value = "数据", required = true, dataTypeClass = PageVo.class)
    })
    public String test(
            @ApiParam(value = "额外的查询参数")
            @RequestParam(required = false) String query,
            @ApiParam(value = "当前页页码",required = true)
            @RequestParam Integer page,
            @ApiParam(value = "每页数据数量",required = true)
            @RequestParam Integer size
    ) {
        Page<AdminInfo> adminInfoPage = new Page<>();
        adminInfoPage.setCurrent(page);
        adminInfoPage.setSize(size);
        List<AdminInfo> adminInfos;
        if (query == null) {
            adminInfos = adminInfoDao.selectAllPage(adminInfoPage).getRecords();
        } else {
            adminInfos = adminInfoDao.selectByQuery(query, adminInfoPage).getRecords();
        }

        if (adminInfos == null) return new GlobalResult<String>(-200, "未找到相关数据", null).toString();

        List<AdminInfoVo> adminInfoVos = new ArrayList<>();
        adminInfos.forEach(v -> adminInfoVos.add(new AdminInfoVo(v.getId(), v.getUserId(), v.getName(), v.getEnable())));

        PageVo<AdminInfoVo> pageVo = new PageVo<>();
        pageVo.setCurrent(page);
        pageVo.setTotal(adminInfoPage.getTotal());
        pageVo.setData(adminInfoVos);
        GlobalResult<PageVo<AdminInfoVo>> result = new GlobalResult<>();
        result.setCode(200);
        result.setData(pageVo);
        result.setMsg("成功！");
        return result.toString();
    }

    @PostMapping("/delete")
    @ApiOperation("删除管理员")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码", example = "200", required = true, dataTypeClass = Integer.class),
            @DynamicParameter(name = "msg", value = "信息", example = "成功", required = true, dataTypeClass = String.class)
    })
    public String testDelete(
            @ApiParam(value = "管理员ID",required = true)
            @RequestParam Long id
    ) {
        adminInfoDao.deleteUserById(id);
        return new GlobalResult<String>(200, "成功", null).toString();
    }

    @PostMapping("/add")
    @ApiOperation("增加管理员")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码", example = "200", required = true, dataTypeClass = Integer.class),
            @DynamicParameter(name = "msg", value = "信息", example = "成功", required = true, dataTypeClass = String.class)
    })
    public String testAdd(
            @ApiParam(value = "AdminInfoVo JSON", required = true)
            @RequestBody AdminInfoVo adminInfoVo
    ) {
        AdminInfo adminInfo = new AdminInfo();
        adminInfo.setUserId(adminInfoVo.getUserId());
        adminInfo.setName(adminInfoVo.getName());
        adminInfo.setPassword(IdUtil.fastSimpleUUID());
        int i = adminInfoDao.insert(adminInfo);
        if (i == 1) return new GlobalResult<String>(200, "成功", null).toString();
        return new GlobalResult<String>(-200, "失败", null).toString();
    }

    @PostMapping("/update")
    @ApiOperation("更新管理员信息")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码", example = "200", required = true, dataTypeClass = Integer.class),
            @DynamicParameter(name = "msg", value = "信息", example = "成功", required = true, dataTypeClass = String.class)
    })
    public String testUpdate(
            @ApiParam(value = "AdminInfoVo JSON", required = true)
            @RequestBody AdminInfoVo adminInfoVo
    ) {
        if (adminInfoVo.getId() == null || adminInfoVo.getId() <= 0)
            return new GlobalResult<String>(-200, "管理员ID必须为大于0的数！", null).toString();
        AdminInfo adminInfo = new AdminInfo();
        adminInfo.setUserId(adminInfoVo.getUserId());
        adminInfo.setName(adminInfoVo.getName());
        adminInfo.setPassword(IdUtil.fastSimpleUUID());
        adminInfoDao.updateById(adminInfo);
        adminInfoDao.setUserEnable(adminInfoVo.getStatus(), adminInfoVo.getId());
        return new GlobalResult<String>(200, "成功", null).toString();
    }
}
