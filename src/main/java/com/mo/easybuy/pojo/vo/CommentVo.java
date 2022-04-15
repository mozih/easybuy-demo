package com.mo.easybuy.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * author mozihao
 * create 2022-03-21 16:07
 * Description
 */
@Data
public class CommentVo {
    private Integer markId;
    private String userName;
    private Integer comId;
    private Integer markScore;
    private String markContent;
    private String markTime;
    private String comDetail;
    private String comPicUrl;
}
