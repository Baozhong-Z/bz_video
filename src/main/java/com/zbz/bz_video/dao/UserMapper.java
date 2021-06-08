package com.zbz.bz_video.dao;

import com.zbz.bz_video.pojo.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface UserMapper extends Mapper<User> {

    String findPasswordById(int uid);
    User userLogin(User user);
    int removeByIds(int[] ids);
    List<User> findAllUserInfo();
}
