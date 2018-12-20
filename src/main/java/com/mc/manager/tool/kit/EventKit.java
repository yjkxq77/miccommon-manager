package com.mc.manager.tool.kit;

import com.mc.manager.bus.event.EventInfo;
import com.vmware.vim25.AlarmState;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.VirtualMachineQuestionInfo;
import com.vmware.vim25.mo.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 服务器事件获取
 *
 * @author Yang jinkang
 * @date 2018/11/16 10:14
 */
public class EventKit {

    /**
     * 服务器事件
     *
     * @return
     * @throws RemoteException
     */
    public List<EventInfo> getEventInfo() throws RemoteException {
        ServiceInstance serviceInstance = CommonalityKit.getService();
        Folder rootFolder = serviceInstance.getRootFolder();
        AlarmManager alarmManager1 = serviceInstance.getAlarmManager();
        ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("HostSystem");
        List<EventInfo> eventInfos = new ArrayList<>();
        if (mes.length > 0) {
            for (int j = 0; j < mes.length; j++) {
                AlarmState[] alarmState = alarmManager1.getAlarmState(mes[j]);
                EventInfo eventInfo = new EventInfo();
                if (alarmState.length > 0) {
                    for (int i = 0; i < alarmState.length; i++) {
                        String alarmStateStatus = alarmState[i].getOverallStatus().toString();
                        if (alarmStateStatus.equals("yellow") || alarmStateStatus.equals("red")) {

                            ManagedObjectReference alarmMO = alarmState[i].alarm;
                            Alarm alarm = new Alarm(alarmManager1.getServerConnection(), alarmMO);
                            String alarmName = alarm.getAlarmInfo().name + "告警";
                            Calendar lastModifiedTime = alarm.getAlarmInfo().getLastModifiedTime();
                            eventInfo.setIp(mes[j].getName());
                            eventInfo.setName(mes[j].getName());
                            eventInfo.setEvent(alarmName);
                            eventInfo.setTime(lastModifiedTime.getTime());
                            eventInfos.add(eventInfo);
                        }
                    }
                }
                HostSystem systems = (HostSystem) mes[j];
                //物理服务器所关联的虚拟机
                VirtualMachine[] virtualMachines = systems.getVms();
                if (virtualMachines.length > 0) {
                    for (int i = 0; i < virtualMachines.length; i++) {
                        VirtualMachine virtualMachine = virtualMachines[i];
                        EventInfo eventInfo1 = new EventInfo();
                        // 告警事件
                        AlarmState[] alarmStateVM = alarmManager1.getAlarmState(virtualMachines[i]);
                        String ipAddress = virtualMachine.getGuest().getIpAddress();

                        if (alarmStateVM.length > 0) {
                            for (int n = 0; n < alarmStateVM.length; n++) {
                                String alarmStateVMStatus = alarmStateVM[n].getOverallStatus().toString();
                                if (alarmStateVMStatus.equals("yellow") || alarmStateVMStatus.equals("red")) {

                                    ManagedObjectReference alarmMO = alarmStateVM[n].alarm;
                                    Alarm alarm = new Alarm(alarmManager1.getServerConnection(), alarmMO);
                                    Calendar lastModifiedTime = alarm.getAlarmInfo().getLastModifiedTime();
                                    String alarmName = alarm.getAlarmInfo().name + "告警";
                                    eventInfo1.setIp(ipAddress);
                                    eventInfo1.setName(virtualMachine.getName());
                                    eventInfo1.setEvent(alarmName);
                                    eventInfo1.setTime(lastModifiedTime.getTime());

                                }
                            }
                        }
                    }
                    for (int i = 0; i < virtualMachines.length; i++) {
                        VirtualMachine virtualMachine = virtualMachines[i];
                        String ipAddress = virtualMachine.getGuest().getIpAddress();
                        String name = virtualMachine.getName();
                        EventInfo eventinfo2 = new EventInfo();

                        /**
                         * 宕机事件
                         */
                        VirtualMachineQuestionInfo question = virtualMachine.getRuntime().getQuestion();
                        if (question != null) {
                            String text = question.getText();
                            eventinfo2.setEvent(text);
                            eventinfo2.setIp(ipAddress);
                            eventinfo2.setName(name);
                            eventInfos.add(eventinfo2);
                        }

                    }

                }
            }
        }
        return eventInfos;
    }


}
