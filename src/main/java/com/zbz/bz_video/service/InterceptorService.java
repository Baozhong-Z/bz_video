package com.zbz.bz_video.service;

import com.zbz.bz_video.dao.RoleResourceMapper;
import com.zbz.bz_video.dao.UserRoleMapper;
import com.zbz.bz_video.pojo.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class InterceptorService {

    @Resource
    UserRoleMapper userRoleMapper;
    @Resource
    RoleResourceMapper roleResourceMapper;

    public int finRoleIdByUid(int uid){
        UserRole userRole = userRoleMapper.selectByPrimaryKey(uid);
        return userRole.getRoleId();
    }

    public List<String> findUrlByRoleId(int roleId){
        List<String> urlByRoleId = roleResourceMapper.findUrlByRoleId(roleId);
        return urlByRoleId;
    }
}
