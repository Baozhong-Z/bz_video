package com.zbz.bz_video.controller;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.dom.PSVIDOMImplementationImpl;
import com.zbz.bz_video.dao.VideoMapper;
import com.zbz.bz_video.dao.VideoTypeMapper;
import com.zbz.bz_video.dao.VtypeMapper;
import com.zbz.bz_video.pojo.User;
import com.zbz.bz_video.pojo.Video;
import com.zbz.bz_video.pojo.VideoType;
import com.zbz.bz_video.pojo.Vtype;
import com.zbz.bz_video.util.Base64Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.text.normalizer.UBiDiProps;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
public class VideoController {

    // 上传文件保存的本地目录
    @Value("${my.accessFile.location}")
    private String location;

    @Resource
    VideoMapper videoMapper;
    @Resource
    VtypeMapper vtypeMapper;
    @Resource
    VideoTypeMapper videoTypeMapper;

    @RequestMapping("/video/{vid}")
    public String toVideo(@PathVariable String vid, Model model){
        Video video = videoMapper.findVideoById(vid);
        model.addAttribute("video", video);
        return "movie-content";
    }

    @RequestMapping("/video/toUpload")
    public String toUpload(Model model){
        List<Vtype> vtypes = vtypeMapper.selectAll();
        model.addAttribute("vTypes", vtypes);
        return "videoUpload";
    }

    @RequestMapping("/video/upload")
    @ResponseBody
    public JSONObject upload(MultipartFile file) throws IOException {
        //上传路径保存设置
        //获得SpringBoot当前项目的路径：System.getProperty("user.dir")
        String path = location+"\\upload\\";

        //按照年月份进行分类：
        Calendar instance = Calendar.getInstance();
        String year = ""+instance.getWeekYear();
        String month = (instance.get(Calendar.MONTH) + 1) + "month";
        path = path + year+"\\"+month+"\\";

        File realPath = new File(path);
        if (!realPath.exists()) {
            boolean res = realPath.mkdirs();
        }

        // 上传文件地址
        System.out.println("上传文件保存地址：" + realPath);

        // 解决文件名字问题：使用uuid;
        String filename = "video-" + UUID.randomUUID().toString().replaceAll("-", "") + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        // 通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        file.transferTo(new File(realPath, filename));

        // 进行回调
        JSONObject res = new JSONObject();
        res.put("url", "/bv/upload/" + year+ "/"+ month + "/" + filename);
        res.put("code", 0);
        res.put("message", "upload success!");

        return res;
    }

    @PostMapping("/video/updateCover")
    @ResponseBody
    public String updateCover(String base64, HttpSession session){
        //获得SpringBoot当前项目的路径：System.getProperty("user.dir")
        String path = System.getProperty("user.dir")+"\\upload\\cover\\";
        File realPath = new File(path);
        if (!realPath.exists()) {
            boolean res = realPath.mkdirs();
        }
        //上传文件地址
        System.out.println("上传封面保存地址：" + realPath);
        //解决文件名字问题：使用uuid;
        String filename = "cv-" + UUID.randomUUID().toString().replaceAll("-", "")+".jpg";
        //输出base64 数据,截取","之后的值进行转换
        base64 = base64.substring(base64.indexOf(",")+1);
        // 将传入的base64转换为jpg图片并保存
        boolean b = Base64Image.Base64ToImage(base64, path + filename);
        if (b){
            // 将生成的文件路径转为url
            String imgUrl = "/upload/cover/" + filename;
            return imgUrl;
        }else {
            return "0";
        }

    }

    @RequestMapping("/video/add")
    @ResponseBody
    public String addVideo(Video video, Integer[] types, HttpSession session){
        Integer uid = (Integer) session.getAttribute("currentUser");
        video.setUid(uid);
        video.setCreateTime(new Date());
        log.info("视频上传参数={}", video);
        int i = videoMapper.insertSelective(video);
        VideoType videoType = new VideoType();
        videoType.setVid(video.getVid());
        for (Integer type : types) {
            videoType.setTid(type);
            videoTypeMapper.insert(videoType);
        }
        return "1";
    }

}
