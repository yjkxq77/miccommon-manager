package com.mc.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 启动类
 *
 * @author LiuChunfu
 * @date 2018/9/6
 */
@Slf4j
@SpringBootApplication
@EnableAsync
@MapperScan(basePackages = {"com.mc.manager.bus.common"}, markerInterface = Mapper.class)
public class VMManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(VMManagerApplication.class);
    }
}
