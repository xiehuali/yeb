package com.syeet.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.syeet.server.mapper.MailLogMapper;
import com.syeet.server.pojo.MailLog;
import com.syeet.server.service.IMailLogService;
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
public class MailLogServiceImpl extends ServiceImpl<MailLogMapper, MailLog> implements IMailLogService {

}
