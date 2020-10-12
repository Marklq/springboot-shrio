package com.dd.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class ShiroController {

    @RequestMapping({"/index", "/"})
    public String index(Model model) {
        model.addAttribute("msg", "hello,shiro");

        return "index";
    }

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/login")
    public String login(String username, String password, Model model) {
        //获取当前的用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            subject.login(token);//执行登录方法，如果没有异常就跳转到首页
            return "index";
        } catch (UnknownAccountException e) {
            model.addAttribute("msg", "用户名错误");
            return "login";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("msg", "密码错误");
            return "login";
        }
    }

    @RequestMapping("/user/add")
    public String add() {
        return "user/add";
    }

    @RequestMapping("/user/modify")
    public String modify() {
        return "user/modify";
    }


    @RequestMapping("/unAuth")
    @ResponseBody
    public String unAuth() {
        return "未授权，请联系管理员授权";
    }


    @RequestMapping("/logout")
    public String logout(HttpServletResponse response, Model model) {
        Subject lvSubject = SecurityUtils.getSubject();
        lvSubject.logout();

        model.addAttribute("msg", "你已经退出");
        return "index";
    }
}
