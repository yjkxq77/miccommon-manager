package com.mc.manager.bus.env.info.ori;

import lombok.Data;

/**
 * 硬盘相关信息
 *
 * @author LiuChunfu
 * @date 2018/3/23
 */
@Data
public class DiskInfo {

    /**
     * 盘符(C/D)
     */
    private String diskLetter;

    /**
     * 硬盘最大空间
     */
    private Long diskMax;

    /**
     * 硬盘已使用大小
     */
    private Long diskUsed;

    /**
     * 硬盘已使用百分比
     */
    private String diskPercent;

}
