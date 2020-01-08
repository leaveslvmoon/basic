package io.itman.admin.intercepter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.itman.admin.vo.User;
import io.itman.library.chunzhenip.IPSeeker;
import io.itman.library.util.IpUtil;
import io.itman.model.Log;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

public class LogInterceptor implements HandlerInterceptor {

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
        User user = (User)request.getSession().getAttribute("user");
        Log log = new Log();
        log.setUserId(user.getAdministrator().getId());
        log.setUrl(request.getRequestURI());
        log.setDatetime(new Date());
        String ip= IpUtil.getIp(request);
        IPSeeker ips=new IPSeeker("qqwry.dat","./chunzhen/");
        log.setIp(ip);
        log.setIpCity(ips.getIPLocation(ip).getCountry());
        String type = request.getHeader("user-agent");
        if(type.contains("Android")) {
            log.setRoleType(2);
            log.setClient("android");
        } else if(type.contains("iPhone")) {
            log.setRoleType(2);
            log.setClient("ios");
        }else{
            log.setRoleType(1);
            log.setClient("pc");
        }
        Map map= request.getParameterMap();
        log.setRequestData(JSONObject.toJSONString(map));
        log.save();

        return true;
    }



}
