package com.zbz.bz_video.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    private Long msgId;
    private String msgTitle;
    private String msgContext;
    private Date msgSendDate;
    private Integer uid;
    private Byte msgStatus;
}
