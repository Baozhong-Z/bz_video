package com.zbz.bz_video.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.zbz.bz_video.dao.*;
import com.zbz.bz_video.pojo.*;
import com.zbz.bz_video.util.Base64Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.thymeleaf.expression.Ids;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleResourceMapper roleResourceMapper;
    @Autowired
    ResourceMapper resourceMapper;
    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    VideoMapper videoMapper;
    @Autowired
    CarouselMapper carouselMapper;

    private Map<String, Object> getLayuiTableDatatMap(List<User> users) {
        PageInfo<User> pageInfo = new PageInfo<>(users);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", pageInfo.getTotal());
        result.put("data", users);
        return result;
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

    @GetMapping("/welcome")
    public String toWelcome(){
        return "admin-welcome";
    }

    @GetMapping("/users")
    public String show(){
        return "admin_userList";
    }

    @GetMapping("/userList")
    @ResponseBody
    public Map<String, Object> showUsers(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "15") Integer limit) {
        PageHelper.startPage(page, limit);
        List<User> users = userMapper.findAllUserInfo();
        return getLayuiTableDatatMap(users);
    }

    @RequestMapping("/user/search")
    @ResponseBody
    public Map<String, Object> searchUser(User user, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "15") Integer limit) {
        PageHelper.startPage(page, limit);
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        if (user.getUid() != null) {
            criteria.andEqualTo("uid", user.getUid());
        }
        if (user.getUserName()!=null && !"".equals(user.getUserName())){
            criteria.andLike("user_name", "%"+user.getUserName()+"%");
        }
        if (user.getGender()!=null && !"".equals(user.getGender())){
            criteria.andEqualTo("gender", user.getGender());
        }
        List<User> userList = userMapper.selectByExample(example);
        return getLayuiTableDatatMap(userList);
    }

    @RequestMapping("/user/add")
    @ResponseBody
    public String addUser(User user) throws Exception{
        boolean isRegistered = this.isRegistered(user.getUserName());
        if (isRegistered){
            return "0";
        }
        final String passwordMd5 = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(passwordMd5);
        user.setRegistrationTime(new Date());
        user.setHeadImg("/image/default_headImg.png");
        userMapper.insertSelective(user);
        UserRole userRole = new UserRole();
        userRole.setUid(user.getUid());
        userRole.setRoleId(2);
        userRoleMapper.insert(userRole);
        return "1";
    }

    @RequestMapping("/user/del/{uid}")
    @ResponseBody
    public String deleteUserById(@PathVariable int uid){
        int i = userMapper.deleteByPrimaryKey(uid);
        if (i>0){
            return "1";
        }else {
            return "0";
        }
    }

    @RequestMapping("/user/delbyids")
    @ResponseBody
    @Transactional
    public String delbyids(String idsJson){
        Gson gson = new Gson();
        int[] ids = gson.fromJson(idsJson, int[].class);
        int result = userMapper.removeByIds(ids);
        if (result == ids.length){
            return "1";
        }else{
            return "0";
        }
    }

    @GetMapping("/video/tovideos")
    public String tovideos(){
        return "videoManage";
    }

    @GetMapping("/video/videoList")
    @ResponseBody
    public Map<String, Object> videoList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit){
        PageHelper.startPage(page, limit);
        Example example = new Example(Video.class);
        example.createCriteria().andNotEqualTo("status", "0");
        List<Video> videos = videoMapper.selectByExample(example);
        PageInfo<Video> pageInfo = new PageInfo<>(videos);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", pageInfo.getTotal());
        result.put("data", videos);
        return result;
    }

    @RequestMapping("/video/changeStatus")
    @ResponseBody
    public String changeStatus(Video video){
        videoMapper.updateByPrimaryKeySelective(video);
        return "1";
    }

    @RequestMapping("/video/delete")
    @ResponseBody
    public String deleteVideo(Integer vid){
        int i = videoMapper.deleteByPrimaryKey(vid);
        return i > 0 ? "1" : "0";
    }

    /**
     * @Author BaoZhong
     * @Description //资源管理页面
     **/
    @GetMapping("/resmanage")
    public String resmanage(){
        return "admin_resourceManage";
    }

    @GetMapping("/resmanage/getallresource/{roleId}")
    @ResponseBody
    public Map<String, Object> getallresource(@PathVariable int roleId){
        Map<String, Object> result = new HashMap<>();
        List<Resource> resources = resourceMapper.selectAll();
        List<Integer> allRelationById = roleResourceMapper.findAllRelationById(roleId);
        result.put("data", resources);
        result.put("value", allRelationById);
        return result;
    }

    @PostMapping("/resmanage/add")
    @ResponseBody
    public String addRoleRes(int roleId, int[] rids){
        int insert = roleResourceMapper.addRelationByIds(roleId, rids);
        if (insert==rids.length){
            return "修改成功";
        }
        return "修改失败";
    }

    @PostMapping("/resmanage/delete")
    @ResponseBody
    public String delRoleRes(int roleId, int[] rids){
        int delete = roleResourceMapper.delRelationByIds(roleId, rids);
        if (delete==rids.length){
            return "修改成功";
        }
        return "修改失败";
    }

    @PostMapping("/rolemanage/edit")
    @ResponseBody
    public String listAllRole(int uid, int roleId){
        UserRole userRole = new UserRole(uid,roleId);
        int i = userRoleMapper.updateByPrimaryKey(userRole);
        if (i>0){
            return "1";
        }else {
            return "0";
        }
    }

    @GetMapping("/video/toExamine")
    public String toExamine(){
        return "videoExamine";
    }

    @GetMapping("/video/examine")
    @ResponseBody
    public List<Video> videoExamine(){
        Example example = new Example(Video.class);
        example.orderBy("status");
        example.createCriteria()
            .andEqualTo("status", 0);
        List<Video> videos = videoMapper.selectByExample(example);
        return videos;
    }

    @PostMapping("/video/pass/{vid}")
    @ResponseBody
    public String pass(@PathVariable int vid){
        Video video = new Video();
        video.setVid(vid);
        video.setStatus(1);
        videoMapper.updateByPrimaryKeySelective(video);
        return "true";
    }

    @PostMapping("/video/refuse/{vid}")
    @ResponseBody
    public String refuse(@PathVariable int vid){
        Video video = new Video();
        video.setVid(vid);
        video.setStatus(2);
        videoMapper.updateByPrimaryKeySelective(video);
        return "true";
    }

    @GetMapping("/video/carousel")
    public String carousel(){
        return "videoCarousel";
    }

    @GetMapping("/video/carousel/showdata")
    @ResponseBody
    public Map<String, Object> showCarouselData(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit){
        PageHelper.startPage(page, limit);
        List<Carousel> carousels = carouselMapper.selectAll();
        PageInfo<Carousel> pageInfo = new PageInfo<>(carousels);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", pageInfo.getTotal());
        result.put("data", carousels);
        return result;
    }

    @GetMapping("/video/carousel/show")
    @ResponseBody
    public List<Carousel> showCarousel(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit){
        List<Carousel> carousels = carouselMapper.selectAll();
        return carousels;
    }

    @RequestMapping("/video/carousel/add")
    @ResponseBody
    public String carouseladd(Carousel carousel){
        carouselMapper.insert(carousel);
        return "1";
    }

    @RequestMapping("/video/carousel/del/{id}")
    @ResponseBody
    public String carouseldel(@PathVariable int id){
        carouselMapper.deleteByPrimaryKey(id);
        return "1";
    }

    @RequestMapping("/video/carousel/delids")
    @ResponseBody
    public String carouseldelids(String idsJson){
        Gson gson = new Gson();
        int[] ids = gson.fromJson(idsJson, int[].class);
        for (int id : ids) {
            carouselMapper.deleteByPrimaryKey(id);
        }
        return "1";
    }

    @PostMapping("/video/carousel/updateCarousel")
    @ResponseBody
    public String updateUserImg(String base64){
        //获得SpringBoot当前项目的路径：System.getProperty("user.dir")
        String path = System.getProperty("user.dir")+"\\upload\\carousel\\";
        File realPath = new File(path);
        if (!realPath.exists()) {
            boolean res = realPath.mkdirs();
        }
        //上传文件地址
        System.out.println("上传轮播图保存地址：" + realPath);
        //解决文件名字问题：使用uuid;
        String filename = "car-" + UUID.randomUUID().toString().replaceAll("-", "")+".jpg";
        //输出base64 数据,截取","之后的值进行转换
        base64 = base64.substring(base64.indexOf(",")+1);
        // 将传入的base64转换为jpg图片并保存
        boolean b = Base64Image.Base64ToImage(base64, path + filename);
        if (b){
            String imgUrl = "/upload/carousel/" + filename;
            return imgUrl;
        }else {
            return "0";
        }

    }


}
