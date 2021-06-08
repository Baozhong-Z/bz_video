package com.zbz.bz_video.controller;

import com.zbz.bz_video.dao.ArticleMapper;
import com.zbz.bz_video.dao.UserMapper;
import com.zbz.bz_video.pojo.Article;
import com.zbz.bz_video.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class ArticleController {

    @Resource
    ArticleMapper articleMapper;

    @GetMapping("/article/addArticle")
    public String toAddArticle(){
        return "article_editor";
    }

    @PostMapping("/article/addArticle")
    public String AddArticle(String content, String title, HttpSession session){
        Article article = new Article();
        article.setArticleTitle(title);
        article.setContent(content);
        article.setPublishTime(new Date());
        int uid = (int)session.getAttribute("currentUser");
        article.setUid(uid);
        articleMapper.insertSelective(article);
        return "redirect:/article/list";
    }

    @GetMapping("/article/list")
    public String articleList(Model model){
        List<Article> articles = articleMapper.findAll();
        model.addAttribute("articles", articles);
        return "article_list";
    }

    @GetMapping("/article/show/{id}")
    public String showArticle(@PathVariable("id") int aid, Model model){
        Article article = articleMapper.findById(aid);
        model.addAttribute("article", article);
        return "article_detail";
    }
}
