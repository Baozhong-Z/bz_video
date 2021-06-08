package com.zbz.bz_video.dao;

import com.zbz.bz_video.pojo.RoleResource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface RoleResourceMapper extends Mapper<RoleResource>{

    List<Integer> findAllRelationById(int roleId);
    int delRelationByIds(@Param("roleId") int roleId, @Param("ids") int[] ids);
    int addRelationByIds(@Param("roleId") int roleId, @Param("ids") int[] ids);
    List<String> findUrlByRoleId(int roleId);
}
