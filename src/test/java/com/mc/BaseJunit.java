package com.mc;

import com.mc.manager.VMManagerApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * 基础测试
 *
 * @author Liu Chunfu
 * @date 2018-12-10 14:53
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VMManagerApplication.class)
@WebAppConfiguration
public class BaseJunit {

}
