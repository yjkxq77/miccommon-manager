package com.mc.manager.tool.kit;

import cn.hutool.core.convert.Convert;
import com.mc.manager.bus.env.info.current.VirtualMachineInfo;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import lombok.extern.slf4j.Slf4j;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VirtualMachine CPU利用率
 * VirtualMachine Memory利用率
 * 获取获取单个VirtualMachine信息
 */
@Slf4j
public class VMKit {

    private VMKit() {
    }

    /**
     * 获取单个虚拟机信息
     *
     * @param virtualMachine
     * @return
     * @throws Exception
     */
    public static VirtualMachineInfo getVMInfo(VirtualMachine virtualMachine) throws Exception {

        VirtualMachineInfo virtualMachineInfo = new VirtualMachineInfo();
        String name = virtualMachine.getName();//虚拟机名称
        int numCPU = virtualMachine.getConfig().getHardware().numCPU;//CPU核数
        ServiceInstance serviceInstance = CommonalityKit.getService();//连接SDK
        List cpuPerList = VMKit.getVMCPUPerByTimeRange(virtualMachine.getName(), serviceInstance, 20, "");
        for (int s = 0; s < cpuPerList.size(); s++) {
            Map m = (Map) cpuPerList.get(s);
            String point = (String) m.get("point");
            PerfMetricSeriesCSV[] csvs = (PerfMetricSeriesCSV[]) m.get("csvs");
            if (point.equals("usage")) {
                PerfMetricSeriesCSV _value = csvs[0];
                int mid1 = Integer.parseInt(_value.getValue()) + 1;
                String value1 = String.valueOf(mid1);
                Float f = Float.valueOf(value1) / 100f;
                String cpuRate = Convert.toStr(f);//CPU使用率
                virtualMachineInfo.setCpuUsageRate(cpuRate);

                VirtualMachinePowerState statusEnum = virtualMachine.getRuntime().powerState;//虚拟机状态
                String statusStr = "normal";
                if (statusEnum.equals(VirtualMachinePowerState.poweredOff)
                        || statusEnum.equals(VirtualMachinePowerState.suspended)) {
                    statusStr = "offline";
                } else if (f > 70F) {
                    statusStr = "alarm";
                }
                virtualMachineInfo.setStatus(statusStr);
            } else if (point.equals("usagemhz")) {
                PerfMetricSeriesCSV _value = csvs[0];
            }
        }

        List netPerList = null;
        netPerList = VMKit.getVMNet(virtualMachine.getName(), 20, "");
        for (int k = 0; k < netPerList.size(); k++) {
            Map m = (Map) netPerList.get(k);
            String point = (String) m.get("point");
            PerfMetricSeriesCSV[] csvs = (PerfMetricSeriesCSV[]) m.get("csvs");
            if (point.equals("bytesRx")) {
                PerfMetricSeriesCSV _value = csvs[0];
                int mid2 = Integer.parseInt(_value.getValue()) + 1;
                String value2 = String.valueOf(mid2);
                virtualMachineInfo.setNetworkRecvRate(value2);//网络接收速率，单位KB/S
            } else if (point.equals("bytesTx")) {
                PerfMetricSeriesCSV _value = csvs[0];
                int mid3 = Integer.parseInt(_value.getValue()) + 1;
                String value3 = String.valueOf(mid3);
                virtualMachineInfo.setNetworkSendRate(value3);//网络发送速率，单位KB/S
            }
        }

        VirtualMachineSummary virtualMachineSummary = virtualMachine.getSummary();
        VirtualMachineStorageSummary virtualMachineStorageSummary = virtualMachineSummary.storage;
        long commit = virtualMachineStorageSummary.committed / 1024;//磁盘总量，单位KB
        long disktotal = commit;//磁盘总量
        double memorySize = (double) virtualMachine.getConfig().getHardware().getMemoryMB(); //物理内存总量，单位MB
        double overallMemoryUsage = (double) virtualMachine.getSummary().quickStats.guestMemoryUsage; //物理内存使用量，单位MB
        float memoryrate = (float) (overallMemoryUsage / memorySize) * 100;
        float memoryrate1 = (float) (Math.round(memoryrate * 100)) / 100;//物理内存使用率
        String ip = virtualMachine.getGuest().getIpAddress();//ip地址
        long memoryTotal = new Double(memorySize).longValue();

        String numCpu = Convert.toStr(numCPU);
        String diskTotal = Convert.toStr(disktotal);
        String memorysize = Convert.toStr(memoryTotal);
        String overAllMemoryUsage = Convert.toStr(overallMemoryUsage);
        String memoryRate = Convert.toStr(memoryrate1);


        virtualMachineInfo.setName(name);
        virtualMachineInfo.setIp(ip);
        virtualMachineInfo.setCpuNums(numCpu);
        virtualMachineInfo.setDiskTotal(diskTotal);
        virtualMachineInfo.setPhysicsMemorySum(memorysize);
        virtualMachineInfo.setPhysicsMemoryUsage(overAllMemoryUsage);
        virtualMachineInfo.setPhysicsMemoryUsageRate(memoryRate);

        return virtualMachineInfo;
    }


    /**
     * 获取cpu内存使用率
     *
     * @param vcomName
     * @param serviceInstance
     * @param timeRange
     * @param instanceName
     * @return
     * @throws Exception
     */
    public static List getVMCPUPerByTimeRange(String vcomName, ServiceInstance serviceInstance, int timeRange, String instanceName) throws Exception {
        List result = new ArrayList();
        InventoryNavigator inventoryNavigator = new InventoryNavigator(serviceInstance.getRootFolder());
        ManagedEntity obj = inventoryNavigator.searchManagedEntity("VirtualMachine", vcomName);
        if (obj == null) {
            throw new RuntimeException("无法获取虚拟机");
        }
        com.vmware.vim25.mo.VirtualMachine vm = (com.vmware.vim25.mo.VirtualMachine) obj;
        PerformanceManager pfMgr = serviceInstance.getPerformanceManager();
        PerfProviderSummary summary = pfMgr.queryPerfProviderSummary(vm);
        PerfInterval[] rfRates = pfMgr.getHistoricalInterval();
        PerfMetricId[] pfMtIds = pfMgr.queryAvailablePerfMetric(vm, null, null, timeRange);
        List<Map> contids = getCpuUsageAndUsageMhzCounterIds(pfMgr, vm, timeRange);

        if (null != contids && contids.size() > 0) {
            int[] counterIds = new int[pfMtIds.length];
            int iLabel = 0;
            for (PerfMetricId pfMt : pfMtIds) {
                int contId = pfMt.getCounterId();
                counterIds[iLabel++] = contId;
            }
            List<Map> instances = new ArrayList<Map>();
            for (PerfMetricId mtId : pfMtIds) {
                int counterId = mtId.getCounterId();

                for (Map m : contids) {
                    Integer cntId = (Integer) m.get("counterId");
                    String groupName = (String) m.get("groupName");
                    String targetName = (String) m.get("targetName");
                    if (counterId == cntId && (null == instanceName || (null != instanceName && mtId.getInstance().equals(instanceName)))) {
                        Map mm = new HashMap();
                        mm.put("counterId", cntId);
                        mm.put("instanceId", mtId.getInstance());
                        mm.put("groupName", groupName);
                        mm.put("targetName", targetName);
                        instances.add(mm);
                    }


                }

            }


            for (int i = 0; i < instances.size(); i++) {
                PerfMetricId[] cpuCores = new PerfMetricId[1];
                Map m = instances.get(i);
                cpuCores[0] = new PerfMetricId();
                cpuCores[0].setCounterId((Integer) m.get("counterId"));
                cpuCores[0].setInstance((String) m.get("instanceId"));
                PerfQuerySpec qSpec = createPerfQuerySpec(vm, cpuCores, 1, timeRange);
                PerfEntityMetricBase[] pValues = pfMgr.queryPerf(new PerfQuerySpec[]{qSpec});
                Map resultM = new HashMap();
                String values = "";
                String lables = "";
                String point = "" + m.get("targetName");
                PerfMetricSeriesCSV[] csvs = null;
                String name = "" + m.get("targetName") + "" + m.get("groupName");
                if (pValues == null) {
                    continue;
                }
                for (int j = 0; j < pValues.length; j++) {
                    PerfEntityMetricCSV csvValue = (PerfEntityMetricCSV) pValues[j];
                    csvs = csvValue.getValue();
                    lables = csvValue.getSampleInfoCSV();
                    for (int k = 0; k < csvs.length; k++) {
                        values += csvs[k].getValue() + ",";
                    }
                }
                resultM.put("name", name);
                resultM.put("values", values);
                resultM.put("lables", lables);
                resultM.put("instance", instanceName);
                resultM.put("point", point);
                resultM.put("csvs", csvs);
                result.add(resultM);
            }
        }
        return result;
    }

    public static PerfQuerySpec createPerfQuerySpec(ManagedEntity me,
                                                    PerfMetricId[] metricIds, int maxSample, int interval) {
        PerfQuerySpec qSpec = new PerfQuerySpec();
        qSpec.setEntity(me.getMOR());
        qSpec.setMaxSample(new Integer(maxSample));
        qSpec.setMetricId(metricIds);
        qSpec.setFormat("csv");
        qSpec.setIntervalId(new Integer(interval));
        return qSpec;
    }

    /**
     * 获取虚拟机cpu辅助方法
     *
     * @param pfMgr
     * @param managedEntity
     * @param point
     * @return
     * @throws RemoteException
     */
    private static List<Map> getCpuUsageAndUsageMhzCounterIds(PerformanceManager pfMgr, ManagedEntity managedEntity, Integer point) throws RemoteException {
        List<Map> result = new ArrayList<Map>();
        PerfMetricId[] pfMtIds = pfMgr.queryAvailablePerfMetric(managedEntity, null, null, point);
        if (null != pfMtIds && pfMtIds.length > 0) {
            int[] counterIds = new int[pfMtIds.length];
            int iLabel = 0;
            for (PerfMetricId pfMt : pfMtIds) {
                int contId = pfMt.getCounterId();
                counterIds[iLabel++] = contId;
            }

            PerfCounterInfo[] pfContinfos = pfMgr.queryPerfCounter(counterIds);
            for (int i = 0; i < pfContinfos.length; i++) {
                String strName = pfContinfos[i].getNameInfo().getKey();
                String groupName = pfContinfos[i].getGroupInfo().getKey();
                if (null != groupName && groupName.equals("cpu")) {
                    if (null != strName && strName.equals("usage") || strName.equals("usagemhz")) {
                        if (result.size() > 0) {
                            boolean flag = true;
                            for (Map mm : result) {
                                Integer key = (Integer) mm.get("counterId");
                                if (key.equals(pfContinfos[i].getKey()))
                                    flag = false;
                            }
                            if (flag) {
                                Map m = new HashMap();
                                m.put("counterId", counterIds[i]);
                                m.put("groupName", groupName);
                                m.put("targetName", strName);
                                result.add(m);
                            }

                        } else {
                            Map m = new HashMap();

                            m.put("counterId", counterIds[i]);
                            m.put("groupName", groupName);
                            m.put("targetName", strName);
                            result.add(m);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取虚拟机网络速率
     *
     * @param vcomName
     * @param timeRange
     * @param instanceName
     * @return
     * @throws Exception
     */
    public static List getVMNet(String vcomName, int timeRange, String instanceName) throws Exception {
        List result = new ArrayList();
        ServiceInstance instance = CommonalityKit.getService();
        InventoryNavigator inventoryNavigator = new InventoryNavigator(instance.getRootFolder());
        ManagedEntity obj = inventoryNavigator.searchManagedEntity("VirtualMachine", vcomName);
        if (obj == null) {
            throw new RuntimeException("获取虚拟机出错！");
        }
        com.vmware.vim25.mo.VirtualMachine vm = (com.vmware.vim25.mo.VirtualMachine) obj;
        PerformanceManager pfMgr = instance.getPerformanceManager();
        PerfProviderSummary summary = pfMgr.queryPerfProviderSummary(vm);
        PerfInterval[] rfRates = pfMgr.getHistoricalInterval();
        PerfMetricId[] pfMtIds = pfMgr.queryAvailablePerfMetric(vm, null, null, timeRange);
        List<Map> contids = getVMNetCounterIds(pfMgr, vm, timeRange);
        if (null != contids && contids.size() > 0) {
            int[] counterIds = new int[pfMtIds.length];
            int iLabel = 0;
            for (PerfMetricId pfMt : pfMtIds) {
                int contId = pfMt.getCounterId();
                counterIds[iLabel++] = contId;
            }
            List<Map> instances = new ArrayList<Map>();
            for (PerfMetricId mtId : pfMtIds) {
                int counterId = mtId.getCounterId();

                for (Map m : contids) {
                    Integer cntId = (Integer) m.get("counterId");
                    String groupName = (String) m.get("groupName");
                    String targetName = (String) m.get("targetName");
                    if (counterId == cntId && (null == instanceName || (null != instanceName && mtId.getInstance().equals(instanceName)))) {
                        Map mm = new HashMap();
                        mm.put("counterId", cntId);
                        mm.put("instanceId", mtId.getInstance());
                        mm.put("groupName", groupName);
                        mm.put("targetName", targetName);
                        instances.add(mm);
                    }
                }

            }

            for (int i = 0; i < instances.size(); i++) {
                PerfMetricId[] netCores = new PerfMetricId[1];
                Map m = instances.get(i);
                netCores[0] = new PerfMetricId();
                netCores[0].setCounterId((Integer) m.get("counterId"));
                netCores[0].setInstance((String) m.get("instanceId"));
                PerfQuerySpec qSpec = createPerfQuerySpec(vm, netCores, 1, timeRange);
                PerfEntityMetricBase[] pValues = pfMgr.queryPerf(new PerfQuerySpec[]{qSpec});
                Map resultM = new HashMap();
                String values = "";
                String lables = "";
                String point = "" + m.get("targetName");
                PerfMetricSeriesCSV[] csvs = null;
                String name = "" + m.get("targetName") + "" + m.get("groupName");
                if (pValues == null) {
                    continue;
                }
                for (int j = 0; j < pValues.length; j++) {
                    PerfEntityMetricCSV csvValue = (PerfEntityMetricCSV) pValues[j];
                    csvs = csvValue.getValue();
                    lables = csvValue.getSampleInfoCSV();
                    for (int k = 0; k < csvs.length; k++) {
                        values += csvs[k].getValue() + ",";
                    }
                }
                resultM.put("name", name);
                resultM.put("values", values);
                resultM.put("lables", lables);
                resultM.put("instance", instanceName);
                resultM.put("point", point);
                resultM.put("csvs", csvs);
                result.add(resultM);
            }
        }
        return result;
    }

    /**
     * 获取虚拟机网络信息的辅助方法
     *
     * @param pfMgr
     * @param managedEntity
     * @param point
     * @return
     * @throws RemoteException
     */
    private static List<Map> getVMNetCounterIds(PerformanceManager pfMgr, ManagedEntity managedEntity, Integer point) throws RemoteException {
        List<Map> result = new ArrayList<Map>();
        PerfMetricId[] pfMtIds = pfMgr.queryAvailablePerfMetric(managedEntity, null, null, point);
        if (null != pfMtIds && pfMtIds.length > 0) {
            int[] counterIds = new int[pfMtIds.length];
            int iLabel = 0;
            for (PerfMetricId pfMt : pfMtIds) {
                int contId = pfMt.getCounterId();
                counterIds[iLabel++] = contId;
            }

            PerfCounterInfo[] pfContinfos = pfMgr.queryPerfCounter(counterIds);
            for (int i = 0; i < pfContinfos.length; i++) {
                String strName = pfContinfos[i].getNameInfo().getKey();
                String groupName = pfContinfos[i].getGroupInfo().getKey();
                if (null != groupName && groupName.equals("net")) {
                    if (null != strName && strName.equals("bytesRx") || strName.equals("bytesTx") || strName.equals("received") || strName.equals("usage")) {
                        if (result.size() > 0) {
                            boolean flag = true;
                            for (Map mm : result) {
                                Integer key = (Integer) mm.get("counterId");
                                if (key.equals(pfContinfos[i].getKey()))
                                    flag = false;
                            }
                            if (flag) {
                                Map m = new HashMap();
                                m.put("counterId", counterIds[i]);
                                m.put("groupName", groupName);
                                m.put("targetName", strName);
                                result.add(m);
                            }

                        } else {
                            Map m = new HashMap();
                            m.put("counterId", counterIds[i]);
                            m.put("groupName", groupName);
                            m.put("targetName", strName);
                            result.add(m);
                        }
                    }
                }
            }
        }
        return result;
    }


    /**
     * 获取所有的虚拟机
     *
     * @return
     * @throws RemoteException
     */
    public static List<VirtualMachine> getAllVM() throws RemoteException {
        long start = System.currentTimeMillis();
        ServiceInstance instance = CommonalityKit.getService();
        InventoryNavigator inventoryNavigator = new InventoryNavigator(instance.getRootFolder());
        ManagedEntity[] mesv = inventoryNavigator.searchManagedEntities("VirtualMachine");
        List<VirtualMachine> virtualMachines = new ArrayList<>(60);
        if (mesv.length > 0) {
            for (ManagedEntity m : mesv) {
                VirtualMachine virtualMachine = (VirtualMachine) m;
                virtualMachines.add(virtualMachine);
            }
        }
        long end = System.currentTimeMillis();
        log.warn("获取所有虚拟机耗时：" + (end - start));
        return virtualMachines;
    }
}
