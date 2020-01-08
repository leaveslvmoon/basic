package io.itman.config;

import com.jfinal.config.Handlers;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.template.ext.spring.JFinalViewResolver;
import com.jfinal.template.source.ClassPathSourceFactory;
import io.itman.admin.direct.ButtonDirective;
import io.itman.admin.direct.SystemNameDirective;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnjoyConfig {
    @Bean(name = "jfinalViewResolver")
    public JFinalViewResolver getJFinalViewResolver() {
        JFinalViewResolver jfr = new JFinalViewResolver();
        // setDevMode 配置放在最前面
        jfr.setDevMode(true);

        // 使用 ClassPathSourceFactory 从 class path 与 jar 包中加载模板文件
        jfr.setSourceFactory(new ClassPathSourceFactory());

        // 在使用 ClassPathSourceFactory 时要使用 setBaseTemplatePath
        // 代替 jfr.setPrefix("/view/")
        JFinalViewResolver.engine.setBaseTemplatePath("/view/");

        jfr.setSuffix(".html");
        jfr.setContentType("text/html;charset=UTF-8");
        jfr.setOrder(0);
        jfr.setRequestContextAttribute("ctx");

        // 支持#(session.value) 的方式访问
        jfr.setSessionInView(true);


        //将StrKit类中所有的public方法添加为shared method，添加完成以后便可以直接在模板中使用
        jfr.addSharedMethod(new com.jfinal.kit.StrKit());


//        jfr.addSharedFunction("/view/common/_layout.html");
//        jfr.addSharedFunction("/view/common/_paginate.html");
        jfr.addDirective("buttonDirective", ButtonDirective.class);
        jfr.addDirective("SystemNameDirective", SystemNameDirective.class);
        return jfr;
    }

    public void configHandler(Handlers me) {
        me.add(new ContextPathHandler("ctx"));
    }

}
