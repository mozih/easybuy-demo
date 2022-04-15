package com.mo.easybuy.mapper;

import com.mo.easybuy.pojo.Browse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mo.easybuy.pojo.vo.BrowseVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mo
 * @since 2022-03-03
 */
public interface BrowseMapper extends BaseMapper<Browse> {

    //根据用户Id查询出按时间顺序排序的商品浏览信息
    List<BrowseVo> getBrowseHistory(Integer userId);

}
