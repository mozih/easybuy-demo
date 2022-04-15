package com.mo.easybuy.service;

import com.mo.easybuy.pojo.Search;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mo
 * @since 2022-03-03
 */
public interface SearchService extends IService<Search> {
    //根据用户id插入搜索记录信息
    int insertSerach(Integer userId,String searchContent);

    //根据用户id查询搜索历史
    List<Search> getSearch(Integer userId);

    //根据用户id和搜索词删除搜索历史，若搜索词为空则清除所有搜索历史
    int deleteSearch(Integer userId,String searchContent);
}
