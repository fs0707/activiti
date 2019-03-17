package org.java.web;

import org.activiti.engine.TaskService;
import org.java.service.PurOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


@Controller
public class PurOrderController {
    @Autowired
    private PurOrderService purOrderService;

    @Autowired
    private TaskService taskService;

    @PostMapping("/createOrder")
    public String createOrder(@RequestParam Map map, HttpSession session){

        //取得用户名
        String userId= (String) session.getAttribute("userId");
        map.put("userId", userId);

        purOrderService.createOrder(map);


        return "/order/ok";

    }

    /***
     * 显示个人待办任务
     * @return
     */
    @GetMapping("/showTask")
    public String showTask(HttpSession ses, Model model){
        String userId =(String) ses.getAttribute("userId");

        List<Map<String,Object>> list = purOrderService.queryPersonTask(userId);
        model.addAttribute("list",list );
        System.out.println("-------------------list-----"+list.size());
        return "order/showTask";
    }

    @GetMapping("/complete/{id}")
    public String completeTask(@PathVariable("id") String id){
        taskService.complete(id);
        return "redirect:/showTask";
    }




}
