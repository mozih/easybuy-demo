package com.mo.easybuy.service;

import com.mo.easybuy.pojo.Mark;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mo.easybuy.pojo.vo.CommentVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mo
 * @since 2022-03-03
 */
public interface MarkService extends IService<Mark> {

    //根据商品Id查询评价信息
    List<CommentVo> selectContent(Integer comId, Integer page);

    //插入评价信息
    int insertMark(Integer userId,Integer comId, String markContent, Integer markScore);

    //根据用户Id查询评价信息
    List<CommentVo> selctContentByUserId(Integer userId);

    //根据评价Id删除评价信息
    int deleteMark(Integer markId);
}
