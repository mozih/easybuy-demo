package com.mo.easybuy.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * author mozihao
 * create 2022-02-28 21:03
 * Description
 */
@Data
public class CommodityVo {
    private Integer comId;
    private String comName;
    private String comDetail;
    private String comAddress;
    private String comUrl;
    private String comPicUrl;
    private Long comScan;
    private Long comMarknumber;
    private BigDecimal priceNow;
    private String priceTime;
}
