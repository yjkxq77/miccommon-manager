package com.mc.manager.bus.log.info;

import lombok.Data;

import java.io.File;

/**
 * 下载文件的包裹信息
 *
 * @author Liu Chunfu
 * @date 2018-11-05 下午4:15
 **/
@Data
public class FileDownWrapper {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件信息
     */
    private File file;

    public FileDownWrapper() {
    }

    public FileDownWrapper(String fileName, File file) {
        this.fileName = fileName;
        this.file = file;
    }
}