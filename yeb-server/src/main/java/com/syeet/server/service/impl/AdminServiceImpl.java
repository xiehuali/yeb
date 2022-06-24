package com.syeet.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.syeet.server.config.security.JwtTokenUtil;
import com.syeet.server.mapper.AdminMapper;
import com.syeet.server.pojo.Admin;
import com.syeet.server.pojo.Menu;
import com.syeet.server.pojo.RespBean;
import com.syeet.server.service.IAdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xhl
 * @since 2022-01-12
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired(required = false)
    private AdminMapper adminMapper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 登录成功，返回token
     *
     * @param username
     * @param password
     * @param code
     * @param request
     * @return
     */
    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {
       //获取放到session中的验证码
        //登录同时需要访问CaptchaController接口/captcha,该接口就实现了把验证码放到session中
        //request.getSession().setAttribute("captcha", text)
        String captcha = (String)request.getSession().getAttribute("captcha");
        if(StringUtils.isEmpty(code) || !captcha.equalsIgnoreCase(code)){
            return RespBean.error("验证码输入错误，请重新输入！");
        }
        //登录
        //security查询数据
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        //前端传过来的明文密码与解析后的数据库密码不一致时
        if (null == userDetails || !passwordEncoder.matches(password, userDetails.getPassword())) {
            return RespBean.error("用户名和密码不正确");
        }
        if (!userDetails.isEnabled()) {
            return RespBean.error("用户被禁用，请联系管理员!");
        }

        //更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(userDetails,
                //凭证，也就是密码，不需要设置
                null,
                //用户权限列表
                userDetails.getAuthorities());
        //security全局上下文，类似于servletContext,保存所有信息
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //生成token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("tokenHead", tokenHead);
        tokenMap.put("token", token);
        return RespBean.success("登录成功", tokenMap);
    }

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return admin
     */
    @Override
    public Admin getAdminByUserName(String username) {
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        //数据库username字段值为username的值
        adminQueryWrapper.eq("username",username);
        //用户没有被禁用
        adminQueryWrapper.eq("enabled",true);
        Admin admin = adminMapper.selectOne(adminQueryWrapper);
        //TODO 健壮性判断
        return admin;
    }


}
