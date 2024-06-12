package com.magictan.service;

import com.magictan.Main;
import com.magictan.model.TestPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;

/**
 * @author: magicTan
 * @time: 2024/6/11
 */
@SpringBootTest(classes =  Main.class)
class TestServiceTest {

    @Autowired
    private TestService testService;

    @Test
    @Sql("classpath:h2.sql")
    public void test() {
        TestPO test1 = new TestPO();
        test1.setId(1);
        this.testService.save(test1);

        TestPO test2 = new TestPO();
        test2.setId(2);
        this.testService.saveBatch(Arrays.asList(test2));
    }



}