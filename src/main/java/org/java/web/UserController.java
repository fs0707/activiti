package org.java.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;


@Controller
public class UserController {

    /**
     * 一加载，进入首页，准备登录
     * @return
     */
    @GetMapping("/init")
    public String init(){
        return "/index";
    }
    @PostMapping("/login")
    public String login(String name, HttpSession ses, Model model){
        if(name.equals("")){
            model.addAttribute("msg","用户名或密码错误");
            return "/index";
        }else{
            ses.setAttribute("userId",name );
            return "/main";
        }
    }

    /**
     * 注销
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpSession ses){
        ses.removeAttribute("userId");
        return "/index";
    }

}
