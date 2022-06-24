package com.syeet.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.syeet.server.mapper.MenuMapper;
import com.syeet.server.pojo.Admin;
import com.syeet.server.pojo.Menu;
import com.syeet.server.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xhl
 * @since 2022-01-12
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired(required = false)
    private MenuMapper menuMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 根据用户id查询菜单列表
     * @return
     */
    @Override
    public List<Menu> getMenusByAdminId() {
        //获取用户id
        //登录之后在全局上下文已经添加了UsernamePasswordAuthenticationToken(UserDetails)
        Integer id = ((Admin)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getId();
        //从redis中取
        ValueOperations valueOperations = redisTemplate.opsForValue();
        List<Menu> menus = (List<Menu>)valueOperations.get("menu_" + id);
        //为空，从数据库中取
        if(CollectionUtils.isEmpty(menus)){
            menus = menuMapper.getMenusByAdminId(id);
            //并且存入redis
            valueOperations.set("menu_"+id,menus);
        }
        return menus;
    }
}
