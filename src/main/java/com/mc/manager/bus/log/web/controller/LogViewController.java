package com.mc.manager.bus.log.web.controller;

import com.mc.manager.bus.log.info.FileDownWrapper;
import com.mc.manager.bus.log.info.LogViewVo;
import com.mc.manager.bus.log.service.LogViewService;
import com.mc.manager.frame.dto.ExecuteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;

/**
 * Log日志查看,在本地下如下所示：
 * --ip
 * --debug
 * --warn
 * -- 2018-11-16 16:31:01.log
 * -- 2018-11-17 16:31:01.log
 *
 * @author Liu Chunfu
 * @date 2018-11-05 下午4:34
 **/
@RestController
@RequestMapping("/mic/manager/log")
public class LogViewController {

    /**
     * 日志服务
     */
    @Autowired
    private LogViewService logViewService;

    /**
     * 获取第一层的路径下的所有ip分类信息。
     *
     * @return
     */
    @GetMapping
    public ExecuteResult<Collection<String>> oneDepthNames() {
        return new ExecuteResult<>(logViewService.localBasePathLs());
    }

    /**
     * 通过第一层的名称，获取对应的第二层分类
     *
     * @param oneDepthName 第一层的名称：ip
     * @return
     */
    @GetMapping("/one-depth")
    public ExecuteResult<Collection<String>> secondTypes(@RequestParam("oneDepthName") String oneDepthName) {
        return new ExecuteResult<>(logViewService.localBaseType(oneDepthName));
    }

    /**
     * 通过第一层的Ip 以及 第二层的分类获取到日志视图
     *
     * @param oneDepthName    第一层的名称：ip
     * @param secondDepthName 第二层名称：type
     * @return 日志视图
     */
    @GetMapping("/one-depth/second-depth")
    public ExecuteResult<Collection<LogViewVo>> thirdDepthLogViews(@RequestParam("oneDepthName") String oneDepthName,
                                                                   @RequestParam("secondDepthName") String secondDepthName) {
        return new ExecuteResult<>(logViewService.localLogViews(oneDepthName, secondDepthName));
    }


    /**
     * 下载指定文件
     *
     * @param oneDepthName    第一层的名称：ip
     * @param secondDepthName 第二层名称：type
     * @param logName         日志名称
     * @return 日志文件资源文件
     */
    @RequestMapping(value = "/one-depth/second-depth/download-log", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadBackupFile(@RequestParam("oneDepthName") String oneDepthName,
                                                       @RequestParam("secondDepthName") String secondDepthName,
                                                       @RequestParam("logName") String logName) {
        FileDownWrapper fileInfo = logViewService.wrapperFile(oneDepthName, secondDepthName, logName);
        return createResource(fileInfo);
    }


    /**
     * 创建下载的resource
     *
     * @param fileInfo 文件包裹信息
     * @return 下载文件包裹类
     */
    private ResponseEntity<org.springframework.core.io.Resource> createResource(FileDownWrapper fileInfo) {
        File file = fileInfo.getFile();
        String fileName = fileInfo.getFileName();
        org.springframework.core.io.Resource body = new FileSystemResource(file);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String header = request.getHeader("User-Agent").toUpperCase();
        HttpStatus status = HttpStatus.CREATED;
        try {
            if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
                // IE下载文件名空格变+号问题
                fileName = fileName.replace("+", "%20");
                status = HttpStatus.OK;
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        //文件名称
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentLength(file.length());
        return new ResponseEntity(body, headers, status);
    }

}
