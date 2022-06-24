package com.syeet.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.syeet.server.mapper.OplogMapper;
import com.syeet.server.pojo.Oplog;
import com.syeet.server.service.IOplogService;
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
public class OplogServiceImpl extends ServiceImpl<OplogMapper, Oplog> implements IOplogService {

}
