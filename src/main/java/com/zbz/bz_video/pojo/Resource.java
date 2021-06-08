package com.zbz.bz_video.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {

    @Id
    private Integer rid;
    private String resource;
    private String description;
}
