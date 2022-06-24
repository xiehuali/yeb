package com.syeet.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syeet.server.pojo.Admin;
import com.syeet.server.pojo.Menu;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xhl
 * @since 2022-01-12
 */
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据用户id查询菜单列表
     * @return
     */
    List<Menu> getMenusByAdminId(@Param("id") Integer id);
}
