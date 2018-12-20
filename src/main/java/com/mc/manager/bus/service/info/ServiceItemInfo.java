package com.mc.manager.bus.service.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务单元的实体类
 *
 * @author Liu Chunfu
 * @date 2018-11-15 下午3:03
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceItemInfo {

    /**
     * 服务Id,唯一标识<br>
     * 必须符合如下规则，即同一个服务实例中只允许1个同类服务
     * <li>ip:port:serviceName </li>
     */
    private String serviceId;

    /**
     * 服务名,也是核心数据容器的Key<br>
     * 可以在不同不同的应用中重复。<br>
     * 逻辑上来看：描述同一类服务
     */
    private String serviceName;

    /**
     * 服务的URL信息<br>
     * 比如：/user/stu
     */
    private String url;

    /**
     * 服务描述
     */
    private String description;


    /**
     * 是否在黑名单中,默认为false.
     *
     * @since 3.1
     */
    private Boolean inBlack = false;

    /**
     * 是否处于错误中，来源于：ServiceErrorStatusManager
     */
    private Boolean inError = false;

    /**
     * 连续失败调用次数
     */
    private Integer repeatFailedTimes;

    /**
     * 服务注册时间，应该是服务端接收到后进行赋值。<br>
     * 考虑应该是服务端接收到服务的时候再更新一次，而不是本地直接上报，HTTP存在误差.
     */
    private Long registrationTime;

    /**
     * 服务更新时间
     */
    private Long updatedTime;

    /**
     * 服务上一次调用时间（错误/成功 均记录）;<br>
     * C# 通过如下代码获取：
     * <pre>
     * TimeSpan ts = DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0, 0);
     * String timestamp= Convert.ToInt64(ts.TotalSeconds).ToString();
     * </pre>
     * Java通过
     * <pre>
     *    System.currentTimeMillis();
     * </pre>
     */
    private Long lastInvokeTimestamp;

    /**
     * 服务这段时间调用次数<br>
     * <notice>提交后清空</notice>
     */
    private Long invokeTimes;

    /**
     * 调用服务的消费耗时(单位ms)<br>
     */
    private Long consumeTime;

    /**
     * 调用失败次数,总次数
     */
    private Integer invokeFailTimes;

    /**
     * 平均耗时
     */
    private double averageConsumeTime;

}
