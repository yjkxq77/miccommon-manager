package com.mc.manager.bus.env.info.current;

import lombok.Data;

/**
 * 当前系统运行快照：CPU、内存、类加载、线程。
 * 其中内存相关信息，单位全部为 M<br>
 * 所有百分比保留2位小数<br>
 *
 * @author LiuChunfu
 * @date 2018/3/23
 */
@Data
public class ServiceEnvInfo {


    /**
     * 应用名称
     *
     * @since 4.0.5 提醒黄总修改那边对应的实体类
     */
    private String appName;

    /**
     * 实例id,通常由ip:port组成。
     */
    private String instanceId;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 状态
     * todo 如何判定是一个后续问题
     * 告警:alarm;正常:normal;离线：offline
     */
    private String status = "normal";

    /**
     * CPU使用率
     */
    private String cpuPercent;

    ////////////内存信息，Java独有，C#为空

    /**
     * 最小堆空间，单位统一为M<br>
     * Java独有，C#为空。
     */
    private String heapInit;

    /**
     * 已提交的堆空间，单位统一为M<br>
     * Java独有，C#为空。
     */
    private String heapCommitted;

    /**
     * 最大的堆容量，单位统一为M<br>
     * Java独有，C#为空。
     */
    private String heapMax;

    /**
     * 堆使用量，单位统一为M<br>
     * Java独有，C#为空。
     */
    private String heapUsed;

    /**
     * 堆空间百分比
     */
    private String heapPercent;


    ///////非堆空间，Java独有，C#为空

    /**
     * 最小非堆空间，单位统一为M<br>
     * Java独有，C#为空。
     */
    private String nonHeapInit;

    /**
     * 非堆区的最大容量，单位统一为M<br>
     * Java独有，C#为空。
     */
    private String nonHeapMax;

    /**
     * 非堆区已使用大小，单位统一为M<br>
     * Java独有，C#为空。
     */
    private String nonHeapUsed;

    /**
     * 非堆区已使用大小，单位统一为M<br>
     * Java独有，C#为空。
     */
    private String nonHeapCommitted;

    /**
     * 非堆的百分比
     */
    private String nonHeapPercent;

    //////系统内存 所有应用都应该上报
    /**
     * 系统最大内存容量
     */
    private String memMax;

    /**
     * 系统内存已使用大小。
     */
    private String memUsed;

    /**
     * 系统内存使用率
     */
    private String memPercent;

    ////////////////线程信息
    /**
     * 线程数量
     */
    private String threadCount;

    /**
     * 守护线程数量
     */
    private String daemonThreadCount;

    /**
     * 最大活动线程数
     */
    private String peakThreadCount;

    /**
     * 系统cpu运行时间
     */
    private String threadCpuTime;

    /**
     * 用户cpu运行时间
     */
    private String threadCpuUserTime;


    //////////////// 类加载信息

    /**
     * 已加载的类的数量
     */
    private String classLoaded;

    /**
     * 总共的加载数量
     */
    private String classTotalLoaded;

    /**
     * 卸载的类的数量
     */
    private String classUnloaded;

    ////////////////其他信息

    /**
     * 当前进程号<br>
     * 可用于后期远程强制关闭
     */
    private String pid;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCpuPercent() {
        return cpuPercent;
    }

    public void setCpuPercent(String cpuPercent) {
        this.cpuPercent = cpuPercent;
    }

    public String getHeapInit() {
        return heapInit;
    }

    public void setHeapInit(String heapInit) {
        this.heapInit = heapInit;
    }

    public String getHeapCommitted() {
        return heapCommitted;
    }

    public void setHeapCommitted(String heapCommitted) {
        this.heapCommitted = heapCommitted;
    }

    public String getHeapMax() {
        return heapMax;
    }

    public void setHeapMax(String heapMax) {
        this.heapMax = heapMax;
    }

    public String getHeapUsed() {
        return heapUsed;
    }

    public void setHeapUsed(String heapUsed) {
        this.heapUsed = heapUsed;
    }

    public String getHeapPercent() {
        return heapPercent;
    }

    public void setHeapPercent(String heapPercent) {
        this.heapPercent = heapPercent;
    }

    public String getNonHeapInit() {
        return nonHeapInit;
    }

    public void setNonHeapInit(String nonHeapInit) {
        this.nonHeapInit = nonHeapInit;
    }

    public String getNonHeapMax() {
        return nonHeapMax;
    }

    public void setNonHeapMax(String nonHeapMax) {
        this.nonHeapMax = nonHeapMax;
    }

    public String getNonHeapUsed() {
        return nonHeapUsed;
    }

    public void setNonHeapUsed(String nonHeapUsed) {
        this.nonHeapUsed = nonHeapUsed;
    }

    public String getNonHeapCommitted() {
        return nonHeapCommitted;
    }

    public void setNonHeapCommitted(String nonHeapCommitted) {
        this.nonHeapCommitted = nonHeapCommitted;
    }

    public String getNonHeapPercent() {
        return nonHeapPercent;
    }

    public void setNonHeapPercent(String nonHeapPercent) {
        this.nonHeapPercent = nonHeapPercent;
    }

    public String getMemMax() {
        return memMax;
    }

    public void setMemMax(String memMax) {
        this.memMax = memMax;
    }

    public String getMemUsed() {
        return memUsed;
    }

    public void setMemUsed(String memUsed) {
        this.memUsed = memUsed;
    }

    public String getMemPercent() {
        return memPercent;
    }

    public void setMemPercent(String memPercent) {
        this.memPercent = memPercent;
    }

    public String getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(String threadCount) {
        this.threadCount = threadCount;
    }

    public String getDaemonThreadCount() {
        return daemonThreadCount;
    }

    public void setDaemonThreadCount(String daemonThreadCount) {
        this.daemonThreadCount = daemonThreadCount;
    }

    public String getPeakThreadCount() {
        return peakThreadCount;
    }

    public void setPeakThreadCount(String peakThreadCount) {
        this.peakThreadCount = peakThreadCount;
    }

    public String getThreadCpuTime() {
        return threadCpuTime;
    }

    public void setThreadCpuTime(String threadCpuTime) {
        this.threadCpuTime = threadCpuTime;
    }

    public String getThreadCpuUserTime() {
        return threadCpuUserTime;
    }

    public void setThreadCpuUserTime(String threadCpuUserTime) {
        this.threadCpuUserTime = threadCpuUserTime;
    }

    public String getClassLoaded() {
        return classLoaded;
    }

    public void setClassLoaded(String classLoaded) {
        this.classLoaded = classLoaded;
    }

    public String getClassTotalLoaded() {
        return classTotalLoaded;
    }

    public void setClassTotalLoaded(String classTotalLoaded) {
        this.classTotalLoaded = classTotalLoaded;
    }

    public String getClassUnloaded() {
        return classUnloaded;
    }

    public void setClassUnloaded(String classUnloaded) {
        this.classUnloaded = classUnloaded;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
