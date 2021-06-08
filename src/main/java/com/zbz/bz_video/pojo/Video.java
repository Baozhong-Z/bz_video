package com.zbz.bz_video.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {

    @Id
    @KeySql(useGeneratedKeys = true)  //主键回填
    private Integer vid;
    private Integer uid;
    private String videoSrc;
    private String videoTitle;
    private String videoDes;
    private Long likes;
    private Long collection;
    private Float score;
    private String cover;
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;
    private Integer status;
    @Transient
    private User user;
}
