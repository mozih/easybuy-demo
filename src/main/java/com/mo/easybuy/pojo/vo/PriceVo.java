package com.mo.easybuy.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * author mozihao
 * create 2022-03-18 15:16
 * Description
 */
@Data
public class PriceVo {
    private Integer priceId;
    private Integer comId;
    private BigDecimal priceNow;
    private String priceTime;
}
