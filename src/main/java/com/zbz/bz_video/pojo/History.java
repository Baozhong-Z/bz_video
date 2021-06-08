package com.zbz.bz_video.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class History {

    @Id
    private Integer uid;
    @Id
    private Long vid;
    private Date historyTime;
    private Integer lastTime;
}
