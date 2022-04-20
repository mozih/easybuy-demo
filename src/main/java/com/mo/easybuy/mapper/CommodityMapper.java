package com.mo.easybuy.mapper;

import com.mo.easybuy.pojo.Commodity;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mo
 * @since 2022-03-03
 */
public interface CommodityMapper extends MyBaseMapper<Commodity> {
    //增加商品浏览次数
    int addComScan(Integer comId);
}
