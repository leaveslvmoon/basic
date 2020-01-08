package io.itman.config;

import com.alibaba.druid.wall.WallFilter;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.source.ClassPathSourceFactory;
import io.itman.model._MappingKit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;

@Configuration
public class ActiveRecordPluginConfig {
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.url}")
    private String url;

    @Bean
    public ActiveRecordPlugin initActiveRecordPlugin() {
        DruidPlugin druidPlugin = new DruidPlugin(url, username, password);
        // 加强数据库安全
        WallFilter wallFilter = new WallFilter();
        wallFilter.setDbType("mysql");
        druidPlugin.addFilter(wallFilter);
        // 添加 StatFilter 才会有统计数据
        // druidPlugin.addFilter(new StatFilter());
        // 必须手动调用start
        druidPlugin.start();

        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        arp.setTransactionLevel(Connection.TRANSACTION_READ_COMMITTED);
        arp.getEngine().setSourceFactory(new ClassPathSourceFactory());
        arp.addSqlTemplate("sql/sqlManager.sql");
        arp.setDevMode(true);
        _MappingKit.mapping(arp);
        arp.setShowSql(true);
        // 必须手动调用start
        arp.start();
        System.out.println("---------jfinal----->");
        return arp;
    }
}
