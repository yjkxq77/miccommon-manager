package com.mc.manager.bus.event;

import com.mc.manager.tool.kit.EventKit;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.List;

/**
 * 事件信息接口实现类
 *
 * @author Yang jinkang
 * @date 2018/11/16 15:20
 */
@Service
public class EventService {

    /**
     * 获取告警信息
     *
     * @return 测试返回
     * @throws RemoteException
     */
    public List<EventInfo> getEvents() throws RemoteException {
        return new EventKit().getEventInfo();
    }
}
