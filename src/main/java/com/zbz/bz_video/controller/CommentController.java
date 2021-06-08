package com.zbz.bz_video.controller;

import com.google.gson.Gson;
import com.zbz.bz_video.dao.CommentArticleMapper;
import com.zbz.bz_video.dao.CommentVideoMapper;
import com.zbz.bz_video.pojo.CommentArticle;
import com.zbz.bz_video.pojo.CommentVideo;
import com.zbz.bz_video.util.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class CommentController {

    @Resource
    CommentVideoMapper commentVideoMapper;
    @Resource
    CommentArticleMapper commentArticleMapper;

    @PostMapping("/video/addcomment")
    @ResponseBody
    public JsonData addVComment4V(Long vid, String content, HttpSession session){
        JsonData jsonData = new JsonData();
        if (session != null){
            int uid = (int) session.getAttribute("currentUser");
            CommentVideo cvideo = new CommentVideo();
            cvideo.setContent(content);
            cvideo.setUid(uid);
            cvideo.setVideoId(vid);
            cvideo.setCreateTime(new Date());
            commentVideoMapper.insertSelective(cvideo);
            jsonData.setStatus("1");
        }
        return jsonData;
    }

    @RequestMapping("/video/loadcomm/{vid}")
    @ResponseBody
    public List<CommentVideo> loadComment4V(@PathVariable long vid){
        List<CommentVideo> comments = commentVideoMapper.findCommentsByVid(vid);
        return comments;
    }


    @PostMapping("/article/addcomment")
    @ResponseBody
    public JsonData addVComment4A(Long aid, String content, HttpSession session){
        JsonData jsonData = new JsonData();
        if (session != null){
            int uid = (int) session.getAttribute("currentUser");
            CommentArticle carticle = new CommentArticle();
            carticle.setContent(content);
            carticle.setUid(uid);
            carticle.setArticleId(aid);
            carticle.setCreateTime(new Date());
            commentArticleMapper.insertSelective(carticle);
            jsonData.setStatus("1");
        }
        return jsonData;
    }

    @RequestMapping("/article/loadcomm/{aid}")
    @ResponseBody
    public List<CommentArticle> loadComment4A(@PathVariable long aid){
        List<CommentArticle> comments = commentArticleMapper.findCommentsByVid(aid);
        return comments;
    }
}
