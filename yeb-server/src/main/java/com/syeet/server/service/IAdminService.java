package com.syeet.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syeet.server.pojo.Admin;
import com.syeet.server.pojo.Menu;
import com.syeet.server.pojo.RespBean;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xhl
 * @since 2022-01-12
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 登录之后返回token
     * @param username
     * @param password
     * @param code
     * @param request
     * @return
     */
    RespBean login(String username, String password, String code, HttpServletRequest request);

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    Admin getAdminByUserName(String username);


}
