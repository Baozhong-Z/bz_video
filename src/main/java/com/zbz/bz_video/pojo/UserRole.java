package com.zbz.bz_video.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {

    @Id
    private Integer uid;
    private Integer roleId;

}
