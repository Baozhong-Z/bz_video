package com.zbz.bz_video.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment_video")
public class CommentVideo {

    @Id
    private Long commentId;
    private Integer uid;
    private Long videoId;
    private String content;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:dd")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    private Date createTime;
    @Transient
    private User user;
}
