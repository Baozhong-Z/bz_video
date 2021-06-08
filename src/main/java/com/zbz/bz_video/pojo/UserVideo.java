package com.zbz.bz_video.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVideo {

    @Id
    private Integer vid;
    @Id
    private Integer uid;
}
