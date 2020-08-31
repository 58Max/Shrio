package com.max.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @RequestMapping("/")
    public String index(){
        return "login";
    }

    @RequestMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        //登出当前账号，清空shiro的缓存
        subject.logout();
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(String username,String password, Model model){
        //获取权限操作对象，利用这个对象来完成登录操作
        Subject subject = SecurityUtils.getSubject();

        //在登录的时候先登出，避免后退缓存没有清除导致不能登录其他账户 可能是用户重新发起重新认证请求
        subject.logout();

        //用户是否认证过（是否登录过）
        if(!subject.isAuthenticated()){
            //认证过程

            //创建用户认证时的身份令牌，并设置从页面传来的用户名和密码
            UsernamePasswordToken token = new UsernamePasswordToken(username,password);
            //执行登录,会自动调用Realm对象中的认证方法 登录失败会抛出各种异常
            try{
                subject.login(token);
            }catch (UnknownAccountException e){
                model.addAttribute("errorMessage","账号错误!");
                return "login";
            }catch (LockedAccountException e){
                model.addAttribute("errorMessage","账号被锁定!");
                return "login";
            }catch (IncorrectCredentialsException e){
                model.addAttribute("errorMessage","密码错误!");
                return "login";
            }
        }
        return "redirect:/success";
    }

    @RequestMapping("/success")
    public String loginSuccess(){
        return "success";
    }

    @RequestMapping("/noPermission")
    public String noPermission(){
        return "noPermission";
    }

    @RequestMapping("/admin/test")
    @ResponseBody
    public String adminTest(){
        return "/admin/test请求";
    }

    @RequestMapping("/user/test")
    @ResponseBody
    public String userTest(){
        return "/user/test请求";
    }

}
