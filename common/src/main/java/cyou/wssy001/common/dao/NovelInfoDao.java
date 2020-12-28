package cyou.wssy001.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cyou.wssy001.common.entity.NovelInfo;
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
public interface NovelInfoDao extends BaseMapper<NovelInfo> {
    @Select("select * from novel_info")
    List<NovelInfo> selectAll();

    @Select("delete from novel_info where id=#{id}")
    void deleteUserById(@Param("id") Long id);

    @Select("update novel_info set enable=#{enable} where id=#{id}")
    void setUserEnable(@Param("enable") Boolean enable, @Param("id") Long id);

    @Select("select * from novel_info where id like CONCAT('%',#{query},'%') or name like CONCAT('%',#{query},'%') or writer like CONCAT('%',#{query},'%')")
    IPage<NovelInfo> selectByQuery(@Param("query") String query, Page<?> page);

    @Select("select * from novel_info")
    IPage<NovelInfo> selectAllPage(Page<?> page);
}
