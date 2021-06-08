package com.zbz.bz_video.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @KeySql(useGeneratedKeys = true)  //主键回填
    private Integer uid;
    private String userName;
    private String password;
    private String gender;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date birthday;
    private String email;
    private String phone;
    private String headImg;
    private String introduction;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date registrationTime;
    @Transient
    private String roleName;
    @Transient
    private Integer roleId;
}
