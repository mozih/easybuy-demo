package com.mo.easybuy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mo.easybuy.mapper.UserMapper;
import com.mo.easybuy.pojo.Mark;
import com.mo.easybuy.mapper.MarkMapper;
import com.mo.easybuy.pojo.vo.CommentVo;
import com.mo.easybuy.service.MarkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class MarkServiceImpl extends ServiceImpl<MarkMapper, Mark> implements MarkService {
    @Resource
    private MarkMapper markMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public List<CommentVo> selectContent(Integer comId, Integer page) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        QueryWrapper<Mark> markQueryWrapper = new QueryWrapper<>();
        markQueryWrapper.eq("comId",comId).orderByDesc("markTime");

        Page<Mark> markPage = new Page<>(page,10);//每页10条数据

        IPage<Mark> markIPage = this.markMapper.selectPage(markPage, markQueryWrapper);

        if (markIPage.getTotal() > 0){
            List<Mark> markList = markIPage.getRecords();
            List<CommentVo> commentVos = new ArrayList<>(markList.size());
            for (Mark mark : markList) {
                CommentVo commentVo = new CommentVo();
                commentVo.setMarkId(mark.getMarkId());
                commentVo.setComId(mark.getComId());
                commentVo.setUserName(this.userMapper.selectById(mark.getUserId()).getUserName());
                commentVo.setMarkContent(mark.getMarkContent());
                commentVo.setMarkScore(mark.getMarkScore());
                commentVo.setMarkTime(simpleDateFormat.format(mark.getMarkTime()));
                commentVos.add(commentVo);
            }
            return commentVos;
        }
        return null;
    }

    @Override
    public int insertMark(Integer userId, Integer comId, String markContent, Integer markScore) {
        //首先查看用户是否已经评价该商品
        QueryWrapper<Mark> markQueryWrapper = new QueryWrapper<>();
        markQueryWrapper.eq("comId",comId).eq("userId",userId);
        List<Mark> markList = this.markMapper.selectList(markQueryWrapper);
        System.out.println("asdfasdfasd"+markList);
        //已经评价该商品
        if (markList.size() > 0){
            return 0;
        }
        Mark mark = new Mark();
        mark.setUserId(userId);
        mark.setComId(comId);
        mark.setMarkContent(markContent);
        mark.setMarkScore(markScore);
        mark.setMarkTime(new Date());
        return this.markMapper.insert(mark);
    }

    @Override
    public List<CommentVo> selctContentByUserId(Integer userId) {
        //使用自定义的mapper查询
        return this.markMapper.selctContentByUserId(userId);
    }

    @Override
    public int deleteMark(Integer markId) {
        return this.markMapper.deleteById(markId);
    }
}
