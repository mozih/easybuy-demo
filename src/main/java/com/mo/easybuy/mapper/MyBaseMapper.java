package com.mo.easybuy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * author mozihao
 * create 2022-04-20 15:20
 * Description
 */
public interface MyBaseMapper<T> extends BaseMapper<T> {
    //批量插入方法
    int insertBatchSomeColumn(List<T> tList);
}
