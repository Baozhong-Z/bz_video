package com.zbz.bz_video.dao;

import com.zbz.bz_video.pojo.CommentArticle;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface CommentArticleMapper extends Mapper<CommentArticle> {

    List<CommentArticle> findCommentsByVid(long aid);
}
