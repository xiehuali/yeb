package com.syeet.server.config.security;

import com.syeet.server.pojo.Admin;
import com.syeet.server.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * security配置类
 * @author xhl
 * @date 2022/1/13
 */
//WebSecurityConfigurerAdapter 类是个适配器, 在配置的时候,需要我们自己写个配置类去继承他,然后编写自己所特殊需要的配置
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    /**
     * 身份认证接口
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //重写这个方法是因为，让登录的时候请求走自己重写的登录方法 UserDetailsService userDetailsService()
        //在这里关联数据库和security
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    /**
     * 强散列哈希加密实现
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        //Security中默认的密码实现  BCryptPasswordEncoder
        return new BCryptPasswordEncoder();
    }


    /**
     * 自定义用户认证逻辑
     */
    @Override
    @Bean
    public UserDetailsService userDetailsService() {
     /*   return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Admin admin = adminService.getAdminByUserName(username);
                if(null != admin){
                    return  admin;
                }
                return null;
            }
        };*/

     //重写了loadUserByUsername()方法
        return username -> {
            //因为admin实现了UserDetails
            Admin admin = adminService.getAdminByUserName(username);
            if (null != admin) {
                return admin;
            }
            return null;
        };
    }

    /**
     *   * 核心过滤器配置方法
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                //放行的资源路径
                "/login",
                "/logout",
                "/css/**",
                "/js/**",
                "/index.html",
                "favicon.ico",
                "/doc.html",
                "/captcha",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/ws/**"
        );
    }

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //使用jwt不需要csrf
        http.csrf()
                .disable()//意思 使残废，关闭后面and之前的配置
                //基于token存储登录用户信息，不需要session，关闭session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //允许访问 上面放行了configure(WebSecurity web)
//                .antMatchers("/login","/logout")
//                .permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest()
                .authenticated()
                .and()
                //禁用缓存
                .headers()
                .cacheControl();

        //添加 jwt 登录授权过滤器
        //把自定义的过滤器jwtAuthenticationTokenFilter放到springsecurity
        // 默认的UsernamePasswordAuthenticationFilter过滤器之前
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权时 和 未登录时处理逻辑
        http.exceptionHandling()
                //自定义 未授权
                .accessDeniedHandler(restfulAccessDeniedHandler)
                //自定义 未登录结果返回
                .authenticationEntryPoint(restAuthorizationEntryPoint);
    }

    //引入自定义的过滤器
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return  new JwtAuthenticationTokenFilter();
    }
}
