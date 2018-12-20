package com.mc.manager.bus.log.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 日志的视图
 *
 * @author Liu Chunfu
 * @date 2018-11-05 下午4:37
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogViewVo {

    /**
     * 名称
     */
    private String name;

    /**
     * 长文件路径
     */
    private String longName;

    /**
     * 大小
     */
    private Long size;
}
