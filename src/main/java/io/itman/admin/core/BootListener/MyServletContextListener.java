package io.itman.admin.core.BootListener;

import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @date 2018/1/6.
 */
@Component
public class MyServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("-------contextInitialized-----------");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("------------contextDestroyed-------------");
    }
}
