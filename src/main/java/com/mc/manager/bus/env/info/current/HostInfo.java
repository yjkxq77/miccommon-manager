package com.mc.manager.bus.env.info.current;

import lombok.Data;

import java.util.List;

/**
 * 主机信息
 *
 * @author Liu Chunfu
 * @date 2018-09-26 下午2:17
 **/
@Data
public class HostInfo {

    //////////// 基本信息
    /**
     * 主机IP
     */
    private String ip;

    /**
     * 主机名称
     */
    private String name;

    /**
     * 状态
     * 告警:alarm;正常:normal;离线：offline
     * 
     */
    private String status;

    /**
     * 虚拟内存总量，单位MB
     * todo
     */
    private String inventedMemorySum;

    /**
     * 虚拟内存已使用量，单位MB
     * todo
     */
    private String inventedMemoryUsage;

    /**
     * 虚拟内存使用率
     * todo
     */
    private String inventedMemoryRate;

    /**
     * 物理内存总量，单位MB
     */
    private String physicsMemorySum;

    /**
     * 物理内存使用量，单位MB
     */
    private String physicsMemoryUsage;

    /**
     * 物理内存使用率
     */
    private String physicsMemoryUsageRate;

    /**
     * 磁盘总量，单位KB
     *
     */
    private String diskTotal;

    /**
     * 磁盘使用量，单位KB
     *
     */
    private String diskUsage;

    /**
     * 磁盘使用率
     *
     */
    private String diskUsageRate;

    /**
     * 网络发送速率，单位KB/S
     *
     */
    private String networkSendRate;

    /**
     * 网络接收速率，单位KB/S
     *
     */
    private String networkRecvRate;

    /**
     * 网络发送总量,单位 KB
     * todo
     */
    private String networkSendTotal;

    /**
     * 网络接收总量,单位 KB
     * todo
     */
    private String networkRecvTotal;

    /**
     * cpu使用率
     */
    private String cpuUsageRate;

    /**
     * cpu名称
     * todo
     */
    private String cpuName;


    /**
     * cpu核数
     */
    private String cpuNums;

    /**
     * 网络带宽
     * todo
     */
    private String speed;

    /**
     * io使用率
     * todo
     */
    private String ioUsage;

    //////////// 关联信息

    /**
     * 虚拟机信息
     */
    private List<VirtualMachineInfo> virtualMachineInfos;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInventedMemorySum() {
        return inventedMemorySum;
    }

    public void setInventedMemorySum(String inventedMemorySum) {
        this.inventedMemorySum = inventedMemorySum;
    }

    public String getInventedMemoryUsage() {
        return inventedMemoryUsage;
    }

    public void setInventedMemoryUsage(String inventedMemoryUsage) {
        this.inventedMemoryUsage = inventedMemoryUsage;
    }

    public String getInventedMemoryRate() {
        return inventedMemoryRate;
    }

    public void setInventedMemoryRate(String inventedMemoryRate) {
        this.inventedMemoryRate = inventedMemoryRate;
    }

    public String getPhysicsMemorySum() {
        return physicsMemorySum;
    }

    public void setPhysicsMemorySum(String physicsMemorySum) {
        this.physicsMemorySum = physicsMemorySum;
    }

    public String getPhysicsMemoryUsage() {
        return physicsMemoryUsage;
    }

    public void setPhysicsMemoryUsage(String physicsMemoryUsage) {
        this.physicsMemoryUsage = physicsMemoryUsage;
    }

    public String getPhysicsMemoryUsageRate() {
        return physicsMemoryUsageRate;
    }

    public void setPhysicsMemoryUsageRate(String physicsMemoryUsageRate) {
        this.physicsMemoryUsageRate = physicsMemoryUsageRate;
    }

    public String getDiskTotal() {
        return diskTotal;
    }

    public void setDiskTotal(String diskTotal) {
        this.diskTotal = diskTotal;
    }

    public String getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(String diskUsage) {
        this.diskUsage = diskUsage;
    }

    public String getDiskUsageRate() {
        return diskUsageRate;
    }

    public void setDiskUsageRate(String diskUsageRate) {
        this.diskUsageRate = diskUsageRate;
    }

    public String getNetworkSendRate() {
        return networkSendRate;
    }

    public void setNetworkSendRate(String networkSendRate) {
        this.networkSendRate = networkSendRate;
    }

    public String getNetworkRecvRate() {
        return networkRecvRate;
    }

    public void setNetworkRecvRate(String networkRecvRate) {
        this.networkRecvRate = networkRecvRate;
    }

    public String getNetworkSendTotal() {
        return networkSendTotal;
    }

    public void setNetworkSendTotal(String networkSendTotal) {
        this.networkSendTotal = networkSendTotal;
    }

    public String getNetworkRecvTotal() {
        return networkRecvTotal;
    }

    public void setNetworkRecvTotal(String networkRecvTotal) {
        this.networkRecvTotal = networkRecvTotal;
    }

    public String getCpuUsageRate() {
        return cpuUsageRate;
    }

    public void setCpuUsageRate(String cpuUsageRate) {
        this.cpuUsageRate = cpuUsageRate;
    }

    public String getCpuName() {
        return cpuName;
    }

    public void setCpuName(String cpuName) {
        this.cpuName = cpuName;
    }

    public String getCpuNums() {
        return cpuNums;
    }

    public void setCpuNums(String cpuNums) {
        this.cpuNums = cpuNums;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getIoUsage() {
        return ioUsage;
    }

    public void setIoUsage(String ioUsage) {
        this.ioUsage = ioUsage;
    }

    public List<VirtualMachineInfo> getVirtualMachineInfos() {
        return virtualMachineInfos;
    }

    public void setVirtualMachineInfos(List<VirtualMachineInfo> virtualMachineInfos) {
        this.virtualMachineInfos = virtualMachineInfos;
    }
}
