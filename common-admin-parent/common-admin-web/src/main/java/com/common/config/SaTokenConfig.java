package com.common.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description Sa-Token权限认证配置，配置登录校验和路由拦截规则
 * @Date 2025/1/7 12:16
 * @Author SparkFan
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    /**
     * 注册Sa-Token拦截器
     *
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册Sa-Token拦截器，校验规则为StpUtil.checkLogin()登录校验
        registry.addInterceptor(new SaInterceptor(handle -> {
            SaRouter
                // 拦截所有路由
                .match("/**")
                // 排除不需要认证的路径
                .notMatch("/auth/login")           // 登录接口
                .notMatch("/auth/register")        // 注册接口
                .notMatch("/auth/captcha")         // 验证码接口
                .notMatch("/doc.html")             // 接口文档
                .notMatch("/swagger-ui/**")        // Swagger UI
                .notMatch("/v3/api-docs/**")       // API文档
                .notMatch("/webjars/**")           // 静态资源
                .notMatch("/favicon.ico")          // 网站图标
                .notMatch("/error")                // 错误页面
                .notMatch("/actuator/**")          // 监控端点
                .notMatch("/druid/**")             // Druid监控
                .notMatch("/upload/**")            // 文件访问
                .notMatch("/static/**")            // 静态资源
                .notMatch("/public/**")            // 公共资源
                // 执行认证函数
                .check(r -> StpUtil.checkLogin());
        })).addPathPatterns("/**");
    }
} 