package com.mc.manager.tool.util;


import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;

/**
 * Url和Uri处理工具类
 *
 * @author LiuChunfu
 * @date 2018/1/24
 */
public final class UrlUtil {

    /**
     * private for reject new instance
     */
    private UrlUtil() {
    }

    /**
     * URL分割标志
     */
    private static final String SPLIT_FLAG = "/";

    /**
     * 分割HTTP获取基础路径的时候，从哪个位置开始寻找
     */
    private static final int START_INDEX = "http://".length();

    /**
     * 拼接URL 最终格式肯定为 /hi/hello <br>
     * 1.前面带有/  2.最后不带/ 3.不以http:// 开头
     *
     * @param first  第一个URL
     * @param others 后续的URL
     * @return 合并后的URL
     */
    public static String combineUri(String first, String... others) {
        StringBuilder builder = new StringBuilder();
        builder.append(SPLIT_FLAG);
        if (StrUtil.isNotBlank(first)) {
            String resolvedStr = solvedSplit(first);
            builder.append(resolvedStr);
            builder.append(SPLIT_FLAG);
        }
        if (others.length != 0) {
            for (String other : others) {
                if (StrUtil.isBlank(other)) {
                    continue;
                }
                builder.append(solvedSplit(other)).append(SPLIT_FLAG);
            }
        }
        return builder.deleteCharAt(builder.length() - 1).toString();
    }

    /**
     * 处理URL前面的 分割符
     *
     * @param oriStr
     * @return 处理后的URL
     */
    private static String solvedSplit(String oriStr) {
        return oriStr.replaceAll(SPLIT_FLAG, "");
    }

    /**
     * Url是带有http的<br>
     * first是：http://register:12000/eureka<br>
     * second是： hello<br>
     * 合并之后的结果是：http://register:12000/eureka/hello<br>
     */
    public static String combineUrl(String first, String others) {
        first = formatUrl(first);
        return URLUtil.complateUrl(first, others);
    }

    /**
     * 解析出基本路径<br>
     * 比如url为 http://register:12000/eureka  --> http://register:12000
     *
     * @param url
     * @return
     */
    public static String baseUrl(String url) {
        url = formatUrl(url);
        int index = url.indexOf(SPLIT_FLAG, START_INDEX);
        return url.substring(0, index + 1);
    }

    /**
     * 获取hostName。比如：https://mp.weixin.qq.com:8080 结果为 mp.weixin.qq.com
     *
     * @param url
     * @return
     */
    public static String hostName(String url) {
        int start = url.indexOf("//");
        int end = url.indexOf(":", start);
        if (end == -1) {
            url = url.substring(start + 2);
            return StrUtil.removeSuffix(url, "/");
        }
        return url.substring(start + 2, end);
    }

    /**
     * 格式化url
     *
     * @param url url
     * @return 格式化
     */
    private static String formatUrl(String url) {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        if (!url.endsWith("/")) {
            url = url + "/";
        }
        return url;
    }

}
