package com.zbz.bz_video.dao;

import com.zbz.bz_video.pojo.Video;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface VideoMapper extends Mapper<Video> {

    Video findVideoById(String vid);
    List<Video> findVideoByType(int tid);
}
