package com.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Web配置
 *
 * @author ${author}
 * @since ${date}
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 允许的跨域源
     */
    @Value("${common.cors-origins:http://localhost:3000}")
    private List<String> corsOrigins;

    /**
     * 文件上传路径
     */
    @Value("${common.upload-path:upload/}")
    private String uploadPath;

    /**
     * 头像上传路径
     */
    @Value("${common.avatar-path:avatar/}")
    private String avatarPath;

    /**
     * 跨域配置
     *
     * @param registry 跨域注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(corsOrigins.toArray(new String[0]))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * 静态资源配置
     *
     * @param registry 资源处理器注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 文件上传路径映射
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + uploadPath);
        
        // 头像路径映射
        registry.addResourceHandler("/avatar/**")
                .addResourceLocations("file:" + avatarPath);
        
        // Knife4j 文档资源映射
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
} 