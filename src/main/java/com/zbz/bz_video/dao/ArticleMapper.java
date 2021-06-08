package com.zbz.bz_video.dao;

import com.zbz.bz_video.pojo.Article;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ArticleMapper extends Mapper<Article> {

    Article findById(int id);

    List<Article> findAll();
}
