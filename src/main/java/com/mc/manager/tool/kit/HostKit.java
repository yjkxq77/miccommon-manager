package com.mc.manager.tool.kit;

import cn.hutool.core.convert.Convert;
import com.mc.manager.bus.env.info.current.HostInfo;
import com.mc.manager.bus.env.info.current.HostNameInfo;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 获取主机信息工具
 *
 * @author Yang jinkang
 * @date 2018/10/11
 */
public class HostKit {
    private InventoryNavigator inventoryNavigator = null;

    /**
     * 获取单个主机信息
     *
     * @param hostSystem
     * @return HostInfo
     * @throws Exception
     */
    public static HostInfo getHostInfo(HostSystem hostSystem) throws Exception {
        ServiceInstance serviceInstance = CommonalityKit.getService();
        String iP = hostSystem.getName();//IP
        String name = hostSystem.getName();  //主机名称
        HostSystemPowerState statusEnum = hostSystem.getRuntime().powerState;
        HostInfo hostInfo = new HostInfo();

        Integer overallCpuUsage = hostSystem.getSummary().getQuickStats().overallCpuUsage;//cpu使用量
        long hz = hostSystem.getHardware().getCpuInfo().getHz() / 1024 / 1024;
        int numcpuCores = hostSystem.getHardware().getCpuInfo().getNumCpuCores();//cpu核数
        long totalCpu = hz * numcpuCores;
        if (overallCpuUsage == null) {
            overallCpuUsage = 0;
        }
        float num = ((float) overallCpuUsage / totalCpu) * 100;
        float cpurate = (float) (Math.round(num * 100)) / 100;//cpu使用率


        double memorysize = (double) hostSystem.getHardware().memorySize / 1024 / 1024; //内存总容量（MB）
        Integer overallMemoryUsage = hostSystem.getSummary().quickStats.overallMemoryUsage; //内存使用容量(MB)
        if (overallMemoryUsage == null) {
            overallMemoryUsage = 0;
        }
        float memoryrate1 = (float) (overallMemoryUsage / memorysize) * 100;
        float memoryrate = (float) (Math.round(memoryrate1 * 100)) / 100;//内存使用率
        long memoryTotal = new Double(memorysize).longValue();

        Datastore[] datastores = hostSystem.getDatastores();
        long datastoretotal = 0;
        long datastoreunused = 0;
        for (int s = 0; s < datastores.length; s++) {
            DatastoreSummary ds = datastores[s].getSummary();
            long capacity = ds.getCapacity() / 1024;//磁盘容量(KB)
            datastoretotal += capacity;
            long unuse = ds.getFreeSpace() / 1024;//磁盘未使用空间(KB)
            datastoreunused += unuse;
        }
        long datastoreused = datastoretotal - datastoreunused;//磁盘使用量(KB)
        float datastoreRate1 = ((float) datastoreused / datastoretotal) * 100;
        float datastoreRate = (float) (Math.round(datastoreRate1 * 100)) / 100;

        String statusStr = "normal";

        if (statusEnum.equals(HostSystemPowerState.poweredOff)
                || statusEnum.equals(HostSystemPowerState.standBy) ||
                statusEnum.equals(HostSystemPowerState.unknown)) {
            statusStr = "offline";
        } else if (cpurate > 70F) {
            statusStr = "alarm";
        }

        HostKit hostKit = new HostKit();
        List hostNetlist = null;
        hostNetlist = hostKit.getHostNetTimeRange(hostSystem.getName(), serviceInstance, 20, "");


        for (int s = 0; s < hostNetlist.size(); s++) {
            Map m = (Map) hostNetlist.get(s);
            String point = (String) m.get("point");
            PerfMetricSeriesCSV[] csvs = (PerfMetricSeriesCSV[]) m.get("csvs");
            if (point.equals("bytesRx")) {
                PerfMetricSeriesCSV _value = csvs[0];
                int mid1 = Integer.parseInt(_value.getValue()) + 1;
                String value1 = String.valueOf(mid1);
                hostInfo.setNetworkRecvRate(value1);
            } else if (point.equals("bytesTx")) {
                PerfMetricSeriesCSV _value = csvs[0];
                int mid2 = Integer.parseInt(_value.getValue()) + 1;
                String value2 = String.valueOf(mid2);
                hostInfo.setNetworkSendRate(value2);
            }
        }

        String memorySize = Convert.toStr(memoryTotal);
        String overAllMemoryUsage = Convert.toStr(overallMemoryUsage);
        String memoryRate = Convert.toStr(memoryrate);
        String cpuRate = Convert.toStr(cpurate);
        String numCpuCores = Convert.toStr(numcpuCores);
        String diskTotal = Convert.toStr(datastoretotal);
        String diskUsage = Convert.toStr(datastoreused);
        String diskUsageRate = Convert.toStr(datastoreRate);


        hostInfo.setName(name);
        hostInfo.setIp(iP);
        hostInfo.setStatus(statusStr);
        hostInfo.setPhysicsMemorySum(memorySize);
        hostInfo.setPhysicsMemoryUsage(overAllMemoryUsage);
        hostInfo.setPhysicsMemoryUsageRate(memoryRate);
        hostInfo.setCpuUsageRate(cpuRate);
        hostInfo.setCpuNums(numCpuCores);
        hostInfo.setDiskTotal(diskTotal);
        hostInfo.setDiskUsage(diskUsage);
        hostInfo.setDiskUsageRate(diskUsageRate);
        return hostInfo;
    }

    /**
     * 获取主机网络发送速率和接收速率
     *
     * @param hostName
     * @param serviceInstance
     * @param timeRange
     * @param instanceName
     * @return
     * @throws Exception
     */
    public List getHostNetTimeRange(String hostName, ServiceInstance serviceInstance, int timeRange, String instanceName) throws Exception {
        List result = new ArrayList();
        ServiceInstance instance = CommonalityKit.getService();
        inventoryNavigator = new InventoryNavigator(instance.getRootFolder());
        ManagedEntity obj = inventoryNavigator.searchManagedEntity("HostSystem", hostName);
        if (obj == null)
            throw new Exception();
        //com.vmware.vim25.mo.VirtualMachine vm = (com.vmware.vim25.mo.VirtualMachine) obj;
        com.vmware.vim25.mo.HostSystem hostSystem = (com.vmware.vim25.mo.HostSystem) obj;
        PerformanceManager pfMgr = instance.getPerformanceManager();
        PerfProviderSummary summary = pfMgr.queryPerfProviderSummary(hostSystem);
        PerfInterval[] rfRates = pfMgr.getHistoricalInterval();
        PerfMetricId[] pfMtIds = pfMgr.queryAvailablePerfMetric(hostSystem, null, null, timeRange);
        List<Map> contids = getVMNetCounterIds(pfMgr, hostSystem, timeRange);
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
                PerfQuerySpec qSpec = createPerfQuerySpec(hostSystem, cpuCores, 1, timeRange);
                PerfEntityMetricBase[] pValues = pfMgr.queryPerf(new PerfQuerySpec[]{qSpec});
                Map resultM = new HashMap();
                String values = "";
                String lables = "";
                String point = "" + m.get("targetName");
                PerfMetricSeriesCSV[] csvs = null;
                String name = "" + m.get("targetName") + "" + m.get("groupName");
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

    public PerfQuerySpec createPerfQuerySpec(ManagedEntity me,
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
     * 获取网络信息的辅助方法
     *
     * @param pfMgr
     * @param managedEntity
     * @param point
     * @return
     * @throws RemoteException
     */
    private List<Map> getVMNetCounterIds(PerformanceManager pfMgr, ManagedEntity managedEntity, Integer point) throws RemoteException {
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
                    if (null != strName && "bytesRx".equals(strName) || "bytesTx".equals(strName)) {
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
     * 获取主机名称
     *
     * @param hostSystem
     * @return
     */
    public static HostNameInfo getHostNameInfo(HostSystem hostSystem) {
        String name = hostSystem.getName();  //主机名称
        HostNameInfo hostNameInfo = new HostNameInfo();
        hostNameInfo.setName(name);

        return hostNameInfo;
    }
}
