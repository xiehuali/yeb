package com.syeet.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.syeet.server.mapper.RoleMapper;
import com.syeet.server.pojo.Role;
import com.syeet.server.service.IRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xhl
 * @since 2022-01-12
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
