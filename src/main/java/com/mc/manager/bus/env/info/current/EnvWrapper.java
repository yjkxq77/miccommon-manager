package com.mc.manager.bus.env.info.current;

import lombok.Data;

import java.util.Collection;
import java.util.List;

/**
 * 上报信息的外层包裹类
 *
 * @author Liu Chunfu
 * @date 2018-09-26 下午1:52
 **/
@Data
public class EnvWrapper {

    /**
     * 主机信息
     */
    private Collection<HostInfo> hostInfo;

    /**
     * 时间戳
     */
    private Long curTimestamp;

    public Collection<HostInfo> getHostInfo() {
        return hostInfo;
    }

    public void setHostInfo(Collection<HostInfo> hostInfo) {
        this.hostInfo = hostInfo;
    }

    public Long getCurTimestamp() {
        return curTimestamp;
    }

    public void setCurTimestamp(Long curTimestamp) {
        this.curTimestamp = curTimestamp;
    }
}
