package com.zbz.bz_video.controller;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.zbz.bz_video.dao.*;
import com.zbz.bz_video.pojo.Carousel;
import com.zbz.bz_video.pojo.User;
import com.zbz.bz_video.pojo.Video;
import com.zbz.bz_video.pojo.Vtype;
import oracle.jrockit.jfr.jdkevents.throwabletransform.ConstructorTracerWriter;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.management.openmbean.SimpleType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author BaoZhong
 * @Description //页面跳转Controller
 * @Date 2021/3/31
 **/
@Controller
public class PageController {

    @Resource
    VideoMapper videoMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    AdminMapper adminMapper;
    @Resource
    VtypeMapper vtypeMapper;
    @Resource
    CarouselMapper carouselMapper;

    @RequestMapping({"/","/index"})
    public String toIndex(Model model){
        // 查询所有视频
        Example example = new Example(Video.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", 1);
        List<Video> videos = videoMapper.selectByExample(example);
        model.addAttribute("allVideo", videos);
        // 查询所有视频类型
        List<Vtype> vtypes = vtypeMapper.selectAll();
        model.addAttribute("vtypes", vtypes);
        // 查询轮播图
        List<Carousel> carousels = carouselMapper.selectAll();
        model.addAttribute("carousels", carousels);
        return "index";
    }

    @RequestMapping("/video/search")
    public String search(String videoName, Model model){
        // 查询所有视频
        Example example = new Example(Video.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", 1)
                .andLike("videoTitle", "%"+videoName+"%");
        List<Video> videos = videoMapper.selectByExample(example);
        model.addAttribute("allVideo", videos);
        // 查询所有视频类型
        List<Vtype> vtypes = vtypeMapper.selectAll();
        model.addAttribute("vtypes", vtypes);
        // 查询轮播图
        List<Carousel> carousels = carouselMapper.selectAll();
        model.addAttribute("carousels", carousels);
        return "index";
    }

    @RequestMapping("/video/type/{tid}")
    public String searchByType(@PathVariable Integer tid, Model model){
        // 查询所有视频
        List<Video> videos = videoMapper.findVideoByType(tid);
        model.addAttribute("allVideo", videos);
        // 查询所有视频类型
        List<Vtype> vtypes = vtypeMapper.selectAll();
        model.addAttribute("vtypes", vtypes);
        // 查询轮播图
        List<Carousel> carousels = carouselMapper.selectAll();
        model.addAttribute("carousels", carousels);
        return "index";
    }

    @GetMapping("/toSignIn")
    public String toLogin(){
        return "movie_loginOrRegister";
    }

    @PostMapping("/signIn")
    @ResponseBody
    public String login(String userName,String password, HttpSession session){
        Map<String, Object> date = new HashMap<>();
        User user = new User();
        final String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setUserName(userName);
        user.setPassword(passwordMd5);
        User userLogin = userMapper.userLogin(user);
        if (userLogin != null){
            session.setAttribute("currentUser", userLogin.getUid());
            date.put("status", 1);
            date.put("msg", "");
        }else {
            date.put("status", 0);
            date.put("msg", "账号或密码错误！");
        }
        return new Gson().toJson(date);
    }

    @GetMapping("/toSignUp")
    public String toSignUp(){
        return "movie_RegisterOrLogin";
    }

    @PostMapping("/signUp")
    @ResponseBody
    public String signUp(String userName, String password1){
        Map<String, Object> date = new HashMap<>();
        User registerUser = new User();
        final String passwordMd5 = DigestUtils.md5DigestAsHex(password1.getBytes());
        registerUser.setUserName(userName);
        registerUser.setPassword(passwordMd5);
        // 判断用户名是否已存在
        boolean isRegistered = this.isRegistered(userName);
        if (isRegistered){
            return "0";
        }
        registerUser.setRegistrationTime(new Date());
        // 设置默认头像
        registerUser.setHeadImg("/image/default_headImg.png");
        userMapper.insertSelective(registerUser);
        return "1";
    }

    @RequestMapping("/checkUserName")
    @ResponseBody
    public String checkUsername(String username){
        boolean isRegistered = this.isRegistered(username);
        Map<String, Object> data = new HashMap<String, Object>();
        if(isRegistered) {
            data.put("isRegistered", true);
            data.put("msg","该账号太受欢迎了，请换一个");
        }else {
            data.put("isRegistered", false);
            data.put("msg","合适的账号");
        }
        return new Gson().toJson(data);
    }

    private boolean isRegistered(String userName){
        User user = new User();
        user.setUserName(userName);
        User user1 = userMapper.selectOne(user);
        boolean isRegistered = false;
        if (user1 != null){
            isRegistered = true;
        }
        return isRegistered;
    }

    /**
     * 管理员登录页面
     */
    @GetMapping({"/admin/login","/admin"})
    public String toAdminLogin() {
        return "adminLogin";
    }

    /**
     * 管理员登录验证
     */
    @PostMapping("/admin/login")
    public String adminlogin(String adminName, String password,HttpSession session, HttpServletRequest request) {
        if ("admin".equals(adminName) && "admin".equals(password)) {
            session.setAttribute("loginAdminRole", -1);
            session.setAttribute("loginAdmin", adminName);
            return "adminIndex";
        } else {
            final String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
            Integer roleId = adminMapper.adminLogin(adminName, passwordMd5);
            if (roleId!=null){
                session.setAttribute("loginAdminRole", roleId);
                session.setAttribute("loginAdmin", adminName);
                return "adminIndex";
            }
            request.setAttribute("msg", "账号或密码错误！");
        }
        return "adminLogin";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "index";
    }

    @RequestMapping("/toError")
    public String error(String msg, Model model){
        model.addAttribute("msg", msg);
        return "/error/error";
    }

}
