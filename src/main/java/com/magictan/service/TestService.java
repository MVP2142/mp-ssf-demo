package com.magictan.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.magictan.mapper.TestMapper;
import com.magictan.model.TestPO;
import org.springframework.stereotype.Service;

/**
 * @author: magicTan
 * @time: 2024/6/11
 */
@Service
public class TestService extends ServiceImpl<TestMapper, TestPO> {
}
