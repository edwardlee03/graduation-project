package cyou.wssy001.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cyou.wssy001.common.entity.AuthCode;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 * @author Tyler
 * @since 2020-11-16
 */
public interface AuthCodeDao extends BaseMapper<AuthCode> {
    @Select("update auth_code set enable=#{enable},status=#{status} where id=#{id}")
    void updateEnableAndStatus(@Param("enable") Boolean enable, @Param("status") String status, @Param("id") Long id);

    @Select("select * from auth_code where code=#{code} and enable=true")
    AuthCode selectOneByCode(@Param("code") String code);
}
