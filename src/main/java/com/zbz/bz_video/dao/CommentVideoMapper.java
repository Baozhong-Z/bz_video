package com.zbz.bz_video.dao;

import com.zbz.bz_video.pojo.CommentVideo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface CommentVideoMapper extends Mapper<CommentVideo> {

    List<CommentVideo> findCommentsByVid(long vid);
}
