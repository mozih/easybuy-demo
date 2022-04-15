package com.mo.easybuy.service;

import com.mo.easybuy.pojo.Price;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mo.easybuy.pojo.vo.PriceVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mo
 * @since 2022-03-03
 */
public interface PriceService extends IService<Price> {
    //根据商品id获得所有价格信息
    List<PriceVo> getPrices(Integer comId);
}
