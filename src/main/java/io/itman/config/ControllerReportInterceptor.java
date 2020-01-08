package io.itman.config;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ControllerReportInterceptor implements HandlerInterceptor {
    private static final ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        if (arg2 instanceof ResourceHttpRequestHandler){

        }else{
            HandlerMethod method = (HandlerMethod) arg2;
            //String requestPath =PathUtil.getRequestPath(request);
            //System.out.println("拦截的请求的请求路径="+requestPath);

            System.out.println("----------ControllerReport--------"+sdf.get().format(new Date())+"-------------------------");
            System.out.println(method.getBean().getClass().getName()+".("+method.getBean().getClass().getSimpleName()+".java:1)");
            System.out.println("method:"+method.getMethod().getName());
            System.out.println("----------ControllerReport-----------------------------------------------------------------");
        }




        return true;
    }

}
