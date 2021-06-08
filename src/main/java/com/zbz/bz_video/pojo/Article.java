package com.zbz.bz_video.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

//文章类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @KeySql(useGeneratedKeys = true)  //主键回填
    private Long articleId; //文章的唯一ID
    private Integer uid; //作者id
    private String articleTitle; //标题
    private String content; //文章的内容
    private Integer likes; // 点赞数
    private Date publishTime; // 发表时间
    @Transient
    private User user; // 所属用户信息

}
