package io.itman.admin.intercepter;


import io.itman.admin.core.annontation.Auth;
import io.itman.admin.vo.User;
import io.itman.model.SysMenu;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 进入controller层之前拦截请求
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("menuUrl",request.getServletPath());
        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            response.sendRedirect("/admin/login");
            return false;
        }
        int roleId= ((User) request.getSession().getAttribute("user")).getAdministrator().getRoleId();
        request.setAttribute("roleId",roleId);
        if (handler instanceof HandlerMethod) {
            // 将handler强转为HandlerMethod, 前面已经证实这个handler就是HandlerMethod
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 从方法处理器中获取出要调用的方法
            Method method = handlerMethod.getMethod();
            Auth auth = method.getAnnotation(Auth.class);
            if (auth == null) {
                // 如果注解为null, 说明不需要拦截, 直接放过
                return true;
            }
            // 如果权限配置不为空, 则取出配置值
            long value = auth.value();
            // 到数据库权限表中查询用户拥有的权限集合, 与当前权限进行对比完成权限校验
            List<SysMenu> menuList= user.getSysmenuList();
            List<Long> values =new ArrayList();
            for(SysMenu m : menuList) {
                values.add(m.getValue());
            }
            // 校验通过返回true, 否则拦截请求
            if(values.contains(value)){
                return true;
            }
        } else {
            return true;
        }
        return false;
    }



}
