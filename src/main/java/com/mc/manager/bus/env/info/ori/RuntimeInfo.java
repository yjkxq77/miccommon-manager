package com.mc.manager.bus.env.info.ori;

import lombok.Data;

/**
 * 运行时的瞬时实践，包括CPU、内存、线程信息
 * <p>
 * 其中内存相关信息，单位全部为 M<br>
 * 所有百分比保留2位小数<br>
 *
 * @author LiuChunfu
 * @date 2018/3/23
 */
@Data
public class RuntimeInfo {

    /**
     * CPU使用率
     */
    private String cpuPercent;

    ////////////内存信息，Java独有，C#为空

    /**
     * 最小堆空间，单位统一为M<br>
     * Java独有，C#为空。
     */
    private Long heapInit;

    /**
     * 已提交的堆空间，单位统一为M<br>
     * Java独有，C#为空。
     */
    private Long heapCommitted;

    /**
     * 最大的堆容量，单位统一为M<br>
     * Java独有，C#为空。
     */
    private Long heapMax;

    /**
     * 堆使用量，单位统一为M<br>
     * Java独有，C#为空。
     */
    private Long heapUsed;

    /**
     * 堆空间百分比
     */
    private String heapPercent;


    ///////非堆空间，Java独有，C#为空

    /**
     * 最小非堆空间，单位统一为M<br>
     * Java独有，C#为空。
     */
    private Long nonHeapInit;

    /**
     * 非堆区的最大容量，单位统一为M<br>
     * Java独有，C#为空。
     */
    private Long nonHeapMax;

    /**
     * 非堆区已使用大小，单位统一为M<br>
     * Java独有，C#为空。
     */
    private Long nonHeapUsed;

    /**
     * 非堆区已使用大小，单位统一为M<br>
     * Java独有，C#为空。
     */
    private Long nonHeapCommitted;

    /**
     * 非堆的百分比
     */
    private String nonHeapPercent;

    //////系统内存 所有应用都应该上报
    /**
     * 系统最大内存容量
     */
    private Long memMax;

    /**
     * 系统内存已使用大小。
     */
    private Long memUsed;

    /**
     * 系统内存使用率
     */
    private String memPercent;

    ////////////////线程信息
    /**
     * 线程数量
     */
    private Integer threadCount;

    /**
     * 守护线程数量
     */
    private Integer daemonThreadCount;

    /**
     * 最大活动线程数
     */
    private Integer peakThreadCount;

    /**
     * 系统cpu运行时间
     */
    private Long threadCpuTime;

    /**
     * 用户cpu运行时间
     */
    private Long threadCpuUserTime;


    //////////////// 类加载信息

    /**
     * 已加载的类的数量
     */
    private Integer classLoaded;

    /**
     * 总共的加载数量
     */
    private Long classTotalLoaded;

    /**
     * 卸载的类的数量
     */
    private Long classUnloaded;

    ////////////////其他信息

    /**
     * 当前进程号<br>
     * 可用于后期远程强制关闭
     */
    private Long pid;

}
