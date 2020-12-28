package cyou.wssy001.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cyou.wssy001.common.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 * @author Tyler
 * @since 2020-11-16
 */
public interface UserInfoDao extends BaseMapper<UserInfo> {
    @Select("select * from user_info")
    List<UserInfo> selectAll();

    @Select("select * from user_info where open_id=#{openId}")
    UserInfo selectUserByOpenId(@Param("openId")String openId);

    @Select("delete from user_info where id=#{id}")
    void deleteUserById(@Param("id") Long id);

    @Select("update user_info set enable=#{enable} where id=#{id}")
    void setUserEnable(@Param("enable") Boolean enable, @Param("id") Long id);

    @Select("select * from user_info where open_id like CONCAT('%',#{query},'%') or qq like CONCAT('%',#{query},'%') or email like CONCAT('%',#{query},'%')")
    IPage<UserInfo> selectByQuery(@Param("query") String query, Page<?> page);

    @Select("select * from user_info")
    IPage<UserInfo> selectAllPage(Page<?> page);
}
