package com.zbz.bz_video.config;

import com.zbz.bz_video.dao.RoleResourceMapper;
import com.zbz.bz_video.dao.UserRoleMapper;
import com.zbz.bz_video.interceptor.CommonInterceptor;
import com.zbz.bz_video.service.InterceptorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Component
@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {

    // 匹配url 中的资源映射
    @Value("my.accessFile.resourceHandler")
    private String resourceHandler;
    // 上传文件保存的本地目录
    @Value("${my.accessFile.location}")
    private String location;

    // Windows下配置
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //匹配到resourceHandler,将URL映射至location,也就是本地文件夹
        registry.addResourceHandler("/bv/**").addResourceLocations("file:///" + "E:/百度云下载/");
        registry.addResourceHandler(("/img/**")).addResourceLocations("file:///" + "E:/百度云下载/图片/");
        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+System.getProperty("user.dir")+"/upload/");
    }

//    // Linux下配置
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        //匹配到resourceHandler,将URL映射至location,也就是本地文件夹
//        registry.addResourceHandler("/bv/**").addResourceLocations("file:" + "/root/download/");
//        registry.addResourceHandler(("/img/**")).addResourceLocations("file:" + "/root/download/image/");
//        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+System.getProperty("user.dir")+"/upload/");
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CommonInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/toSignIn",
                        "/signIn",
                        "/toSignUp",
                        "/signUp",
                        "/logout",
                        "/toError",
//                        "/video/*",
                        "/video/loadcomm/**",
                        "/video/type/**",
                        "/admin/",
                        "/admin/login",
                        "/checkUserName",
                        "/",
                        "/index",
                        "/upload/**",
                        "/img/**",
                        "/error",
                        "/bv/**",
                        "*.mp4",
                        "*.jpg",
                        "*.png",
                        "*.html",
                        "/image/**",
                        "/images/**",
                        "/upload_img/**",
                        "/favicon.ico",
                        "/layui/**",
                        "/lib/**",
                        "/api/**",
                        "/**.js",
                        "*.css",
                        "/js/**",
                        "/css/**");
    }
}
