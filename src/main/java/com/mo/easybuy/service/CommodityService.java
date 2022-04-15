package com.mo.easybuy.service;

import com.mo.easybuy.pojo.Commodity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mo.easybuy.pojo.vo.CommodityVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mo
 * @since 2022-03-03
 */
public interface CommodityService extends IService<Commodity> {
    //根据keyword获得商品信息,并且根据商城名
    List<CommodityVo> getCommodity(String keyword,String[] shops);
    //根据商品id获得商品信息
    Commodity getCommodityById(Integer comId);
    //更新评价数量
    int updateMarkById(Integer comId);
}
