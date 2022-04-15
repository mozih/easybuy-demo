package com.mo.easybuy.pojo.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * author mozihao
 * create 2022-03-26 14:55
 * Description
 */
@Data
public class BrowseVo {
    private Integer browseId;
    private Integer comId;
    private String comDetail;
    private String comPicUrl;
    private String comAddress;
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String browseTime;
}
