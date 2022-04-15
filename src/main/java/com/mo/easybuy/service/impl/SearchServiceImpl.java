package com.mo.easybuy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mo.easybuy.pojo.Search;
import com.mo.easybuy.mapper.SearchMapper;
import com.mo.easybuy.service.SearchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mo
 * @since 2022-03-03
 */
@Service
public class SearchServiceImpl extends ServiceImpl<SearchMapper, Search> implements SearchService {
    @Resource
    private SearchMapper searchMapper;

    @Override
    public int insertSerach(Integer userId, String searchContent) {
        Search search = new Search();
        search.setUserId(userId);
        search.setSearchContent(searchContent);
        search.setSearchTime(new Date());
        return this.searchMapper.insert(search);
    }

    @Override
    public List<Search> getSearch(Integer userId) {
        QueryWrapper<Search> searchQueryWrapper = new QueryWrapper<>();
        searchQueryWrapper.eq("userId",userId).orderByDesc("searchTime");
        List<Search> searches = this.searchMapper.selectList(searchQueryWrapper);

        //只获取前10个
        List<Search> searchList = new ArrayList<>(10);
        //临时存放搜索历史，避免重复
        HashSet<String> searchString = new HashSet<>(10);

        for (Search search : searches) {
            //判断set中是否存在该搜索关键词
            if (searchString.add(search.getSearchContent()) && searchList.size() < 10){
                searchList.add(search);
            }
        }
        return searchList;
    }

    @Override
    public int deleteSearch(Integer userId, String searchContent) {
        QueryWrapper<Search> searchQueryWrapper = new QueryWrapper<>();
        searchQueryWrapper.eq("userId",userId);
        //如果条件不为空，删除特定的搜索历史
        if (null != searchContent){
            searchQueryWrapper.eq("searchContent",searchContent);
            return this.searchMapper.delete(searchQueryWrapper);
        }
        return this.searchMapper.delete(searchQueryWrapper);
    }
}
