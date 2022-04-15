package com.mo.easybuy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mo.easybuy.pojo.Price;
import com.mo.easybuy.mapper.PriceMapper;
import com.mo.easybuy.pojo.vo.PriceVo;
import com.mo.easybuy.service.PriceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class PriceServiceImpl extends ServiceImpl<PriceMapper, Price> implements PriceService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private PriceMapper priceMapper;

    @Override
    public List<PriceVo> getPrices(Integer comId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //由于查询商品的时候已经把商品价格列表都存到redis中，因此直接从redis中获取
        List<Price> priceList = (List<Price>) this.redisTemplate.opsForValue().get("prices:"+comId);
        //如果为空则从数据库中获取
        if (priceList == null){
            QueryWrapper<Price> priceQueryWrapper = new QueryWrapper<>();
            priceQueryWrapper.eq("comId", comId).orderByDesc("priceTime");
            priceList = this.priceMapper.selectList(priceQueryWrapper);
            //存入redis中
            this.redisTemplate.opsForValue().set("prices:" + comId, priceList);
        }

        //将priceList转换成priceVoList并按时间正序排列
        List<PriceVo> priceVoList = new ArrayList<>(priceList.size());
        for (int i = priceList.size()-1; i >= 0; i--) {
            PriceVo priceVo = new PriceVo();
            priceVo.setPriceId(priceList.get(i).getPriceId());
            priceVo.setComId(priceList.get(i).getComId());
            priceVo.setPriceNow(priceList.get(i).getPriceNow());
            priceVo.setPriceTime(simpleDateFormat.format(priceList.get(i).getPriceTime()));
            priceVoList.add(priceVo);
        }
        return priceVoList;
    }
}
