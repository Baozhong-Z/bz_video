package com.zbz.bz_video;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author BaoZhong
 * @Description 启动类
 * @Date 2021/3/31
 * @Param
 * @return
 **/
@SpringBootApplication
@MapperScan("com.zbz.bz_video.dao")
public class BzVideoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BzVideoApplication.class, args);
    }

}
