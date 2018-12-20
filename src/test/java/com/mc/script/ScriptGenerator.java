package com.mc.script;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.Test;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 测试脚本
 *
 * @author Liu Chunfu
 * @date 2018-11-23 10:59
 **/
public class ScriptGenerator {

    @Test
    public void testStart() {
        List<String> lines = FileUtil.readLines("deploy/start-template.sh", CharsetUtil.UTF_8);
        String collect = Optional.ofNullable(lines).orElse(new ArrayList<>()).stream()
                .map(this::replaceOrClean)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.joining(System.lineSeparator()));

        System.out.println(collect);
    }


    @Test
    public void testClean() {
        String templateStr = FileUtil.readString("deploy/stop-template.sh", CharsetUtil.UTF_8);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("mic-app", "test-auto-deployAll");
        String format = StrUtil.format(templateStr, paramMap);
        System.out.println(format);
    }

    private String replaceOrClean(String oriStr) {
        if (oriStr.startsWith("##")) {
            return "";
        }
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("mic-app", "test-auto-deployAll");
        paramMap.put("java-home", "/mic-core-tools/jdk1.8.0_171/jre/bin/java");
        paramMap.put("base-path", "/mic-auto-deployAll");
        paramMap.put("java-param", "");

        String format = StrUtil.format(oriStr, paramMap);
        return format;
    }

    @Test
    public void testChangeToUtf8(){
        String path="/Users/liuchunfu/git-project/case-analysis/src/main/java/io/netty/cases/chapter";
        List<File> files = FileUtil.loopFiles(path);
        files.forEach(this::change);
    }

    private void change(File file){
        String content = FileUtil.readString(file, CharsetUtil.GBK);
        FileUtil.writeString(content,file,CharsetUtil.UTF_8);
    }
}
