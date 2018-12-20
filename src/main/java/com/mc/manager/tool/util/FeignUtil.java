package com.mc.manager.tool.util;

import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 * Feign的Util
 *
 * @author Liu Chunfu
 * @date 2018-08-07 上午9:42
 **/
public final class FeignUtil {


    private FeignUtil() {

    }

    /**
     * 创建对应的url
     *
     * @param url   目标的基础路径
     * @param clazz api的clazz
     * @param <T>
     * @return API
     */
    public static <T> T createInstance(String url, Class<T> clazz) {
        T target = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(3_000, 10_000))
                .retryer(new Retryer.Default(3_000, 5_000, 3))
                .target(clazz, url);
        return target;
    }
}
