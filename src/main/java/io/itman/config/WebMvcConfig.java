package io.itman.config;

import io.itman.admin.intercepter.LogInterceptor;
import io.itman.admin.intercepter.LoginInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.MultipartConfigElement;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

/**
 * @date 2018/1/1.
 * spring shiro
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Value("${rm.imagePath}")
    private String imagePath;
    @Value("${rm.uploadPath}")
    private String filePath;
    @Value("${controllerReport}")
    private int controllerReport;

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }




    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(responseBodyConverter());
        converters.add(new MappingJackson2HttpMessageConverter());

    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);
        registry.addResourceHandler("/plugin/**", "/static/**")
                .addResourceLocations("classpath:/plugin/", "classpath:/static/");
        registry.addResourceHandler("/xb/**", "/static/**")
                .addResourceLocations("classpath:/xb/", "classpath:/static/");
        registry.addResourceHandler("/ftl/**").addResourceLocations("classpath:/ftl/");
        registry.addResourceHandler("/images/**").addResourceLocations("file:" + imagePath);
        registry.addResourceHandler("/file/**").addResourceLocations("file:" + filePath);
        super.addResourceHandlers(registry);
    }

    /*保留国际化*/
    @Bean
    public LocaleChangeInterceptor interceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor());
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/admin/**").excludePathPatterns("/admin/login","/admin/login/main","/member/register","/member/getCode","/member/sendMsg").excludePathPatterns("/api/**","/images/**","/plugin/**","/image/**","/error/**");
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/admin/**").excludePathPatterns("/admin/login","/admin/login/main").excludePathPatterns("/api/**","/images/**","/plugin/**","/image/**");
        //根据配置文件状态，决定是否开启controllerReport
        if(1==controllerReport){
            registry.addInterceptor(new ControllerReportInterceptor()).addPathPatterns("/**").excludePathPatterns("/plugin/**","/images/**","/image/**","/error/**");
        }

        //拦截api请求
//        registry.addInterceptor(new ApiInterceptor()).addPathPatterns("/api/**");
//        registry.addInterceptor(new ApiInterceptor()).addPathPatterns("/papi/**");
//        registry.addInterceptor(new ShopInterceptor()).addPathPatterns("/shop/**").excludePathPatterns("/shop/login","/shop/");
    }

    @Bean
    public LocaleResolver resolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.US);
        return resolver;
    }

    /**
     * 文件上传配置
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        factory.setMaxFileSize("50MB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("50MB");
        return factory.createMultipartConfig();
    }


}
