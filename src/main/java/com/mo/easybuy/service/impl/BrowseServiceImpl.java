package com.mo.easybuy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mo.easybuy.mapper.CommodityMapper;
import com.mo.easybuy.pojo.Browse;
import com.mo.easybuy.mapper.BrowseMapper;
import com.mo.easybuy.pojo.Commodity;
import com.mo.easybuy.pojo.vo.BrowseVo;
import com.mo.easybuy.service.BrowseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mo
 * @since 2022-03-03
 */
@Service
public class BrowseServiceImpl extends ServiceImpl<BrowseMapper, Browse> implements BrowseService {

    @Resource
    private BrowseMapper browseMapper;
    @Resource
    private CommodityMapper commodityMapper;

    @Override
    public int saveBrowse(Integer comId, Integer userId) {
        //增加商品浏览次数
        this.commodityMapper.addComScan(comId);

        //添加浏览记录
        Browse browse = new Browse();
        browse.setBrowseTime(new Date());
        browse.setComId(comId);
        browse.setUserId(userId);
        return this.browseMapper.insert(browse);
    }

    @Override
    public List<BrowseVo> getBrowseList(Integer userId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //获得浏览历史，根据时间正序排序
        List<BrowseVo> browseVos = this.browseMapper.getBrowseHistory(userId);
        List<BrowseVo> browseVoList = new ArrayList<>();

        //临时存放浏览的商品Id，使用set避免重复
        Set<Integer> comIds = new HashSet<>(browseVos.size());

        //对浏览历史列表进行处理,获取同一商品浏览信息时间最新的
        for (BrowseVo browseVo : browseVos) {
//            System.out.println(browseVo.getBrowseId() + ":" + browseVo.getComId() + ":" + browseVo.getBrowseTime());
            //判断set中是否存在该商品信息
            if (comIds.add(browseVo.getComId())){
                //将时间转换为yyyy-MM-dd
                browseVo.setBrowseTime(browseVo.getBrowseTime().substring(0,11));
                //不存在则添加成功
                browseVoList.add(browseVo);
            }
        }
        return browseVoList;
    }
}
