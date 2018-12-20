package com.mc.manager.bus.env.web.controller;

import com.mc.manager.bus.env.info.current.VirtualMachineInfo;
import com.mc.manager.bus.env.service.vm.VirtualMachineService;
import com.mc.manager.frame.dto.ExecuteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * 虚拟机的请求接口
 *
 * @author Liu Chunfu
 * @date 2018-10-16 下午2:34
 **/
@RequestMapping("/mic/manager/virtual-machine")
@RestController
public class VirtualMachineController {


    @Autowired
    private VirtualMachineService virtualMachineService;


    /**
     * 获取所有的虚拟机信息
     *
     * @return 虚拟机信息
     * @throws Exception
     */
    @GetMapping("/all")
    public ExecuteResult<List<VirtualMachineInfo>> allVirtualMachines() throws Exception {
        return  new ExecuteResult<>(virtualMachineService.getAllVirtualMachineInfos());
    }

    /**
     * 根据{@arg virtualName} 获取对应的虚拟机信息
     *
     * @param vmName 虚拟机名称
     * @throws Exception
     */
    @GetMapping
    public ExecuteResult<VirtualMachineInfo> virtualMachineInfo(@RequestParam("vmName") String vmName) throws Exception {
        return new ExecuteResult<>(virtualMachineService.getVirtualMachineInfoByVMName(vmName));
    }

    /**
     * 关机
     *
     * @param vmName
     */
    @DeleteMapping("/power")
    public ExecuteResult<String> powerOff(@RequestParam("vmName") String vmName) throws MalformedURLException, RemoteException {
        return new ExecuteResult<>(virtualMachineService.powerOff(vmName));
    }

    /**
     * 开机
     *
     * @param vmName
     * @param hostName
     */
    @PostMapping("/power")
    public ExecuteResult<String> powerOn(@RequestParam("vmName") String vmName, @RequestParam("hostName") String hostName) throws MalformedURLException, RemoteException {
            String result = virtualMachineService.powerOn(vmName, hostName);
            ExecuteResult<String> executeResult = new ExecuteResult<>();
            executeResult.setResult(result);
            return executeResult;
    }

    /**
     * 重启
     *
     * @param vmName
     */
    @PutMapping("/power")
    public ExecuteResult<String> vmRestart(@RequestParam("vmName") String vmName) throws MalformedURLException, RemoteException {

        return new ExecuteResult<>(virtualMachineService.reset(vmName));
    }


}
