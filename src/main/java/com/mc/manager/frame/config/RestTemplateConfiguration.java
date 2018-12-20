package com.mc.manager.frame.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

/**
 * 构建RestTemplate对象
 *
 * @author LiuChunfu
 * @date 2018/1/15
 */
@Slf4j
@Configuration
public class RestTemplateConfiguration {

    /**
     * 不加负载均衡的RestTemplate<br>
     * 直接走连接不通过拦截器处理
     *
     * @return
     */
    @Bean
    public RestTemplate pureRestTemplate() {
        OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory();
        //此处后续可以调整和优化
        //requestFactory.setReadTimeout(50000);
        //requestFactory.setConnectTimeout(50000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        // 使用 utf-8 编码集的 convert 替换默认的 convert（默认的 string convert 的编码集为 "ISO-8859-1"）
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = messageConverters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if (converter instanceof StringHttpMessageConverter) {
                iterator.remove();
            }
        }

        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        log.debug("【原生HTTP调用器】初始化单一的RestTemplate完成");
        return restTemplate;
    }

}
