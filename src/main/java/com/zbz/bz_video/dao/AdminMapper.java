package com.zbz.bz_video.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMapper {

    Integer adminLogin(@Param("loginName") String loginName,@Param("password") String password);
}
