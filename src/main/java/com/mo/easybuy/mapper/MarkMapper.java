package com.mo.easybuy.mapper;

import com.mo.easybuy.pojo.Mark;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mo.easybuy.pojo.vo.CommentVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mo
 * @since 2022-03-03
 */
public interface MarkMapper extends BaseMapper<Mark> {
    //根据用户Id查询评价信息
    List<CommentVo> selctContentByUserId(Integer userId);

}
