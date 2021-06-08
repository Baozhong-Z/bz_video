package com.zbz.bz_video.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Transient;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResource {

    @Id
    private Integer rid;
    @Id
    private Integer roleId;
    @Transient
    private String roleName;
    @Transient
    private String resourceDes;
}
