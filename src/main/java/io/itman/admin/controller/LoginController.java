package io.itman.admin.controller;


import io.itman.admin.service.LoginService;
import io.itman.library.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**

* @Description:    登录管理

* @Author:         CES

* @Version:        1.0

*/
@Controller
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    LoginService loginService;
    /**
     * @Author CES
     * @Description 跳转登录页面
     * @Param []
     * @return java.lang.String
     **/
    @RequestMapping("/login")
    public String login(){
        return ("login");
    }

    /**
     * @Author CES
     * @Description 登录动作
     * @Param [request]
     * @return java.lang.String
     **/
    @PostMapping("login/main")
    public String login(HttpServletRequest request){
        String login = loginService.login(request);
        return login;
    }

    /**
     * @Author CES
     * @Description 登出动作
     * @Param [request]
     * @return java.lang.String
     **/
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            session.removeAttribute("user");
            return "login";
        }
        return "login";
    }

    /**
     * @Author CES
     * @Description 跳转首页
     * @Param [request]
     * @return java.lang.String
     **/
    @GetMapping("/index")
    public String index(HttpServletRequest request) {
        loginService.index(request);
        return ("index_new");
    }


    /**
     * @Author CES
     * @Description 跳转修改密码页面
     * @Param [request]
     * @return java.lang.String
     **/
    @GetMapping("/password")
    public String password(HttpServletRequest request) {
        return "password/password";
    }

    /**
     * 密码信息保存
     *
     * @param
     * @returnPassword:123456 NewPWD:1234567
     * ConfirmPWD:1234568
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public JsonUtil save(HttpServletRequest request, String password, String newPwd, String confirmPwd) {
        JsonUtil result = loginService.save(request,password,newPwd,confirmPwd);
        return result;
    }

    /**
     * @Author CES
     * @Description 后台首页
     * @Param []
     * @return java.lang.String
     **/
    @GetMapping(value = "/right")
    public String  right(){

        return "right";
    }
}
