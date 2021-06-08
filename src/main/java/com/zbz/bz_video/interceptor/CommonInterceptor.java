package com.zbz.bz_video.interceptor;

import com.zbz.bz_video.dao.RoleResourceMapper;
import com.zbz.bz_video.dao.UserRoleMapper;
import com.zbz.bz_video.pojo.UserRole;
import com.zbz.bz_video.service.InterceptorService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
@Component
public class CommonInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        InterceptorService interceptorService = this.getMapper(InterceptorService.class, request);
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession();
        if (requestURI.startsWith("/admin")){
            Integer roleId = (Integer) session.getAttribute("loginAdminRole");
            if (roleId!=null){
                if (roleId==-1){
                    return true;
                }else {
                    boolean flag = false;
                    final List<String> adurls = interceptorService.findUrlByRoleId(roleId);
                    for (String url : adurls) {
                        if (url.contains(requestURI)){
                            flag = true;
                            break;
                        }
                    }
                    if (flag){
                        return true;
                    }else {
                        request.setAttribute("msg", "您没有相关的权限");
                        request.getRequestDispatcher("/toError").forward(request, response);
                        return false;
                    }
                }
            }else {
                response.sendRedirect("/admin/");
                return false;
            }
        }else {
        Integer uid = (Integer) session.getAttribute("currentUser");
        if (uid!=null){
            int userRoleId = interceptorService.finRoleIdByUid(uid);
            if (userRoleId==-1){
                return true;
            }
            final List<String> urls = interceptorService.findUrlByRoleId(userRoleId);
            boolean flag = false;
            for (String url : urls) {
                if (url.contains(requestURI)){
                    flag = true;
                    break;
                }
            }
            if (flag){
                return true;
            }else {
                request.setAttribute("msg", "您没有相关的权限");
                request.getRequestDispatcher("/toError").forward(request, response);
                return false;
            }
        }else {
            response.sendRedirect("/toSignIn");
        }}
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private <T> T getMapper(Class<T> clazz,HttpServletRequest request)
    {
        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        return factory.getBean(clazz);
    }
}
