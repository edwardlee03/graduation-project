package cyou.wssy001.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import cyou.wssy001.common.dao.UserInfoDao;
import cyou.wssy001.common.entity.GlobalResult;
import cyou.wssy001.common.entity.UserInfo;
import cyou.wssy001.common.vo.PageVo;
import cyou.wssy001.common.vo.UserInfoVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: graduation-project
 * @ClassName: UserController
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/12/22
 * @Version v1.0
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserInfoDao userInfoDao;

    @PostMapping("/")
    @ApiOperation("查询用户 - 分页")
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
        Page<UserInfo> userInfoPage = new Page<>();
        userInfoPage.setCurrent(page);
        userInfoPage.setSize(size);
        List<UserInfo> userInfos;
        if (query == null) {
            userInfos = userInfoDao.selectAllPage(userInfoPage).getRecords();
        } else {
            userInfos = userInfoDao.selectByQuery(query, userInfoPage).getRecords();
        }

        if (userInfos == null) return new GlobalResult<String>(-200, "未找到相关数据", null).toString();

        List<UserInfoVo> userInfoVos = new ArrayList<>();
        userInfos.forEach(v -> userInfoVos.add(new UserInfoVo(v.getId(), v.getOpenId(), v.getQq(), v.getEmail(), v.getEnable())));

        PageVo<UserInfoVo> pageVo = new PageVo<>();
        pageVo.setCurrent(page);
        pageVo.setTotal(userInfoPage.getTotal());
        pageVo.setData(userInfoVos);
        GlobalResult<PageVo<UserInfoVo>> result = new GlobalResult<>();
        result.setCode(200);
        result.setData(pageVo);
        result.setMsg("成功！");
        return result.toString();
    }

    @PostMapping("/delete")
    @ApiOperation("删除用户")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码", example = "200", required = true, dataTypeClass = Integer.class),
            @DynamicParameter(name = "msg", value = "信息", example = "成功", required = true, dataTypeClass = String.class)
    })
    public String testDelete(
            @ApiParam(value = "用户ID", required = true)
            @RequestParam Long id
    ) {
        userInfoDao.deleteUserById(id);
        return new GlobalResult<String>(200, "成功", null).toString();
    }

    @PostMapping("/insert")
    @ApiOperation("测试增加")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码", example = "200", required = true, dataTypeClass = Integer.class),
            @DynamicParameter(name = "msg", value = "信息", example = "成功", required = true, dataTypeClass = String.class)
    })
    public String testInsert() {
        UserInfo userInfo = new UserInfo();
        for (int i = 0; i < 10; i++) {
            userInfo.setOpenId("testOpenId" + i);
            userInfo.setQq("testQQ" + i);
            userInfo.setEmail("testEmail" + i);
            userInfo.setEnable(i % 2 == 0);
            userInfoDao.insert(userInfo);
        }
        return new GlobalResult<String>(200, "成功", null).toString();
    }

    @PostMapping("/add")
    @ApiOperation("增加用户")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码", example = "200", required = true, dataTypeClass = Integer.class),
            @DynamicParameter(name = "msg", value = "信息", example = "成功", required = true, dataTypeClass = String.class)
    })
    public String testAdd(
            @ApiParam(value = "UserInfoVo JSON", required = true)
            @RequestBody UserInfoVo userInfoVo
    ) {
        UserInfo userInfo = new UserInfo();
        userInfo.setOpenId(userInfoVo.getOpenId());
        userInfo.setQq(userInfoVo.getQq());
        userInfo.setEmail(userInfoVo.getEmail());
        userInfo.setEnable(userInfoVo.getStatus());
        int i = userInfoDao.insert(userInfo);
        if (i == 1) return new GlobalResult<String>(200, "成功", null).toString();
        return new GlobalResult<String>(-200, "失败", null).toString();
    }

    @PostMapping("/update")
    @ApiOperation("更新用户信息")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码", example = "200", required = true, dataTypeClass = Integer.class),
            @DynamicParameter(name = "msg", value = "信息", example = "成功", required = true, dataTypeClass = String.class)
    })
    public String testUpdate(
            @ApiParam(value = "UserInfoVo JSON", required = true)
            @RequestBody UserInfoVo userInfoVo
    ) {
        if (userInfoVo.getId() == null || userInfoVo.getId() <= 0)
            return new GlobalResult<String>(-200, "用户ID必须为大于0的数！", null).toString();
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userInfoVo.getId());
        userInfo.setOpenId(userInfoVo.getOpenId());
        userInfo.setQq(userInfoVo.getQq());
        userInfo.setEmail(userInfoVo.getEmail());
        userInfoDao.updateById(userInfo);
        userInfoDao.setUserEnable(userInfoVo.getStatus(), userInfoVo.getId());
        return new GlobalResult<String>(200, "成功", null).toString();
    }
}
