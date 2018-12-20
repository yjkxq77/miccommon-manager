package com.mc.manager.bus.event;

import com.mc.manager.frame.dto.ExecuteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Yang jinkang
 * @Description:
 * @date 2018/11/16 16:30
 */
@RestController
@RequestMapping("/mic/manager/event")
public class EventController {
    /**
     * 注入eventService
     */
    @Autowired
    private EventService eventService;

    /**
     * 获取告警信息
     *
     * @return
     * @throws RemoteException
     */
    @GetMapping
    public ExecuteResult<List<EventInfo>> getHostEvent() throws RemoteException {
        return new ExecuteResult<>(eventService.getEvents());
    }


}
