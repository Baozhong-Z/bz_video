package com.zbz.bz_video.controller;

import com.google.gson.Gson;
import com.zbz.bz_video.dao.UserMapper;
import com.zbz.bz_video.dao.VideoMapper;
import com.zbz.bz_video.pojo.User;
import com.zbz.bz_video.pojo.Video;
import com.zbz.bz_video.util.Base64Image;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Controller
public class UserController {

    @Resource
    UserMapper userMapper;
    @Resource
    VideoMapper videoMapper;

    @GetMapping("/user/homepage")
    public String toHome(HttpSession session, Model model){
        int uid = (int)session.getAttribute("currentUser");
        User user = getUserSecret(uid);
        model.addAttribute("user", user);
        Example example = new Example(Video.class);
        example.createCriteria()
                .andEqualTo("uid", uid)
                .andNotEqualTo("status", 0);
        List<Video> myVideos = videoMapper.selectByExample(example);
        model.addAttribute("myVideos", myVideos);
        return "home";
    }

    @GetMapping("/user/home/{uid}")
    public String toUserHome(@PathVariable int uid, Model model){
        User user = getUserSecret(uid);
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/user/editInfo")
    @ResponseBody
    public User toEditInfo(HttpSession session){
        int uid = (int)session.getAttribute("currentUser");
        User user = userMapper.selectByPrimaryKey(uid);
        return user;
    }

    @PostMapping("/user/editInfo")
    public String editInfo(User user, Model model){
        int uid = user.getUid();
        int i = userMapper.updateByPrimaryKeySelective(user);
        User user2 = getUserSecret(uid);
        model.addAttribute("user", user2);
        return "home";
    }

    @PostMapping("/user/updateUserImg")
    @ResponseBody
    public String updateUserImg(String base64, HttpSession session){
        //获得SpringBoot当前项目的路径：System.getProperty("user.dir")
        String path = System.getProperty("user.dir")+"/upload/headImg/";
        File realPath = new File(path);
        if (!realPath.exists()) {
            boolean res = realPath.mkdirs();
        }
        //上传文件地址
        System.out.println("上传头像保存地址：" + realPath);
        //解决文件名字问题：使用uuid;
        String filename = "hd-" + UUID.randomUUID().toString().replaceAll("-", "")+".jpg";
        //输出base64 数据,截取","之后的值进行转换
        base64 = base64.substring(base64.indexOf(",")+1);
        // 将传入的base64转换为jpg图片并保存
        boolean b = Base64Image.Base64ToImage(base64, path + filename);
        if (b){
            // 更新用户数据
            int uid = (int)session.getAttribute("currentUser");
            User user = new User();
            user.setUid(uid);
            // 将生成的文件路径转为url
            String imgUrl = "/upload/headImg/" + filename;
            user.setHeadImg(imgUrl);
            userMapper.updateByPrimaryKeySelective(user);
            return imgUrl;
        }else {
            return "0";
        }

    }

    private User getUserSecret(int uid) {
        User user = userMapper.selectByPrimaryKey(uid);
        String phone = user.getPhone();
        if (phone != null && !"".equals(phone)){
            StringBuilder stringBuilder = new StringBuilder(phone);
            String res = stringBuilder.replace(3, 7, "****").toString();
            user.setPhone(res);
        }
        return user;
    }
}
