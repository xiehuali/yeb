package com.syeet.server.controller;


import com.syeet.server.pojo.Menu;
import com.syeet.server.pojo.RespBean;
import com.syeet.server.service.IAdminService;
import com.syeet.server.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xhl
 * @since 2022-01-12
 */
@RestController
@RequestMapping("/system/cfg")
@Api(tags = "菜单")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @ApiOperation(value = "根据用户id查询菜单列表")
    @GetMapping(value = "/menu")
    public List<Menu> getMenusById(){
        return menuService.getMenusByAdminId();
    }

}
