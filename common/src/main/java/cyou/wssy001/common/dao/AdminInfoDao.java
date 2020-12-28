package cyou.wssy001.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cyou.wssy001.common.entity.AdminInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 * @author Tyler
 * @since 2020-12-22
 */
public interface AdminInfoDao extends BaseMapper<AdminInfo> {
    @Select("select * from admin_info")
    List<AdminInfo> selectAll();

    @Select("delete from admin_info where id=#{id}")
    void deleteUserById(@Param("id") Long id);

    @Select("update admin_info set enable=#{enable} where id=#{id}")
    void setUserEnable(@Param("enable") Boolean enable, @Param("id") Long id);

    @Select("select * from admin_info where id like CONCAT('%',#{query},'%') or name like CONCAT('%',#{query},'%')")
    IPage<AdminInfo> selectByQuery(@Param("query") String query, Page<?> page);

    @Select("select * from admin_info")
    IPage<AdminInfo> selectAllPage(Page<?> page);
}
