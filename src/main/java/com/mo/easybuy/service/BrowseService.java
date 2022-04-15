package com.mo.easybuy.service;

import com.mo.easybuy.pojo.Browse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mo.easybuy.pojo.vo.BrowseVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mo
 * @since 2022-03-03
 */
public interface BrowseService extends IService<Browse> {

    //存储浏览记录
    int saveBrowse(Integer comId,Integer userId);

    //根据userId获得浏览记录
    List<BrowseVo> getBrowseList(Integer userId);
}
