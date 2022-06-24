package com.syeet.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syeet.server.pojo.Admin;
import com.syeet.server.pojo.Menu;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xhl
 * @since 2022-01-12
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 根据用户id查询菜单列表
     * @return
     */
    List<Menu> getMenusByAdminId();
}
