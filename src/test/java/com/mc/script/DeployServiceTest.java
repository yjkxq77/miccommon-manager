package com.mc.script;

import com.jcraft.jsch.Session;
import com.mc.BaseJunit;
import com.mc.manager.bus.deploy.service.DeployService;
import com.mc.manager.tool.util.JschUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 服务部署的测试
 *
 * @author Liu Chunfu
 * @date 2018-12-10 14:52
 **/
public class DeployServiceTest extends BaseJunit {

    @Autowired
    private DeployService deployService;

    @Test
    public void testUpload() {
        //resourceDeployService.deployJarAndScript(getSession(), "/Volumes/Repository/temp/deployAll/fatjar/register-server", "/mic-core-tools3/12/ffa/55");
        System.out.println("success");
    }

    @Test
    public void deployJavaTest() {
        boolean b = deployService.environmentReady(getSession());
        System.out.println(b);
    }

    @Test
    public void testDeployJdk() {
        deployService.deployJavaEnvironment(getSession());
        System.out.println("SUCCESS");
    }

    @Test
    public void testAll() {
        deployService.deployAll(getSession(), "/Volumes/Repository/temp/deploy/hello/hello-8090.jar", null);
        System.out.println("SUCCESS");
    }

    private Session getSession() {
        return JschUtil.getSession("10.5.21.28", 22, "root", "1qaz2wsx");
    }
}
