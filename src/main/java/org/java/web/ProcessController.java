package org.java.web;

import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.InputStream;


@Controller
public class ProcessController {

    @Autowired
    private RepositoryService repositoryService;
    /**
     * 此方法，用于处理页面间的跳转
     * @param folder
     * @param target
     * @return
     */
    @GetMapping("/forward/{folder}/{target}")
    public String forward(@PathVariable("folder") String folder,@PathVariable("target") String target){
        return folder+"/"+target;
    }

    /***
     * 部署流程定义
     * @return
     */
    @RequestMapping("/deploy")
    public String deployDefinition(
            @RequestParam("bpmn") MultipartFile bpmn,
            @RequestParam("png") MultipartFile png) throws Exception {

        System.out.println(repositoryService==null);

        // 得到输入流
        InputStream bpmn_in = bpmn.getInputStream();
        InputStream png_in = png.getInputStream();

        // 得到文件的名称
        String bpmn_name = bpmn.getOriginalFilename();
        String png_name = png.getOriginalFilename();

        repositoryService.createDeployment().addInputStream(bpmn_name, bpmn_in)
                .addInputStream(png_name, png_in).deploy();

        System.out.println("部署成功................................");

        return "/process/deployOk";
    }

}
