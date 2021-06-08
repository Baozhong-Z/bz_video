package com.zbz.bz_video;

import com.google.gson.Gson;
import com.zbz.bz_video.dao.*;
import com.zbz.bz_video.pojo.*;
import com.zbz.bz_video.util.Base64Image;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest
class BzVideoApplicationTests {

    @Resource
    ArticleMapper articleMapper;
    @Resource
    VideoMapper videoMapper;
    @Resource
    CommentVideoMapper commentVideoMapper;
    @Autowired
    RoleResourceMapper roleResourceMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    VideoTypeMapper videoTypeMapper;

    @Test
    void contextLoads() {
        Article article = articleMapper.findById(1);
        System.out.println(article);
        final String passwordMd5 = DigestUtils.md5DigestAsHex("123456".getBytes());
        System.out.println(passwordMd5);
        List<Article> all = articleMapper.findAll();
        for (Article article1 : all) {
            System.out.println("=====================================");
            System.out.println(article1);
        }
    }

    @Test
    void videoMapperTest(){
        Video video = videoMapper.findVideoById("1");
        System.out.println(video);
    }

    @Test
    void commentVideoMapperTest(){
        List<CommentVideo> comments = commentVideoMapper.findCommentsByVid(1L);
        for (CommentVideo comment : comments) {
            System.out.println(comment);
        }
        String json = new Gson().toJson(comments);
        System.out.println(json);
    }

    @Test
    void base64ToimageTest(){
        final String base64 = Base64Image.ImageToBase64ByLocal("E:\\百度云下载\\图片\\背景.png");
        boolean b = Base64Image.Base64ToImage(base64, "D://666.jpg");
        System.out.println(b);
    }

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Test
    void getAllUrl(){
        Map<RequestMappingInfo, HandlerMethod> map = this.handlerMapping.getHandlerMethods();
        Set<RequestMappingInfo> set = map.keySet();
        System.out.println("total: "+set.size());
        for (RequestMappingInfo info : set) {
            HandlerMethod handlerMethod = map.get(info);
            // springmvc的url地址，不包含项目名
            PatternsRequestCondition patternsCondition = info.getPatternsCondition();
            System.out.println(patternsCondition);
        }
    }

    @Test
    void quanxian(){
        List<Integer> idList = roleResourceMapper.findAllRelationById(4);
        System.out.println(idList);
    }

    @Test
    void mutiParamTest(){
        int[] ids = {3,4};
        int i = roleResourceMapper.delRelationByIds(4, ids);
        System.out.println(i);
    }

    @Test
    void mutiParamTest2(){
        int ids[] = {1,2};
        int i = roleResourceMapper.addRelationByIds(4, ids);
        System.out.println(i);
    }

    @Test
    void userMapperTest(){
        List<User> allUserInfo = userMapper.findAllUserInfo();
        for (User user : allUserInfo) {
            System.out.println(user);
        }
    }

//    @Test
//    void typeTest(){
//        VideoType videoType = new VideoType();
//        for (int i=1;i<11; i++){
//            for (int j = 1; j <11 ; j++) {
//                videoType.setVid(i);
//                videoType.setTid(j);
//                videoTypeMapper.insert(videoType);
//            }
//        }
//    }

}
