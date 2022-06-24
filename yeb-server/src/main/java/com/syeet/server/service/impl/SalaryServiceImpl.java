package com.syeet.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.syeet.server.mapper.SalaryMapper;
import com.syeet.server.pojo.Salary;
import com.syeet.server.service.ISalaryService;
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
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary> implements ISalaryService {

}
