package com.mo.easybuy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mo.easybuy.mapper.CommodityMapper;
import com.mo.easybuy.mapper.PriceMapper;
import com.mo.easybuy.pojo.Commodity;
import com.mo.easybuy.pojo.Price;
import com.mo.easybuy.pojo.vo.CommodityVo;
import com.mo.easybuy.utils.crawler.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * author mozihao
 * create 2022-03-05 20:45
 * Description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private CommodityMapper commodityMapper;
    @Resource
    private PriceMapper priceMapper;

    @Test
    public void redisTest() throws ParseException {
        //设置key序列化器
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        //获得商品信息
        String keyword = "";
        List<CommodityVo> commodityVoList = Crawler.crawler(keyword);

        //存储商品信息，使用redis
        for (CommodityVo commodityVo : commodityVoList) {
            //第一层保护，如果redis中存在商品信息则不用进入到数据库中保存了
            //首先在redis中查询是否有该商品,以商品名称作为key，整个商品信息为value
            if (null == this.redisTemplate.opsForValue().get("commodity:" + commodityVo.getComName())) {
                //在commodityVoList中筛选出商品信息
                Commodity commodity = new Commodity();
                commodity.setComUrl(commodityVo.getComUrl());
                commodity.setComAddress(commodityVo.getComAddress());
                commodity.setComDetail(commodityVo.getComDetail());
                commodity.setComName(commodityVo.getComName());

                //第二层保护，先查询数据库中是否有该商品信息，有就不插入，防止商品信息重复
                //创建查询条件
                QueryWrapper<Commodity> wrapper = new QueryWrapper<>();
                //写入查询条件,查询数据库中是否有该商品
                wrapper.eq("comName", commodity.getComName());
                //如果数据库中没有该商品信息，则插入到数据库中
                if (null == commodityMapper.selectOne(wrapper)) {
                    commodityMapper.insert(commodity);
                }
                //重新查询商品信息，取得商品id
                commodity = commodityMapper.selectOne(wrapper);
                //将商品信息存储到redis中
                this.redisTemplate.opsForValue().set("commodity:" + commodityVo.getComName(), commodity);
            }
        }

        /**
         * 存储价格信息，不适用redis，相同的商品价格可能会因为优惠而改变，因此先查询
         * 数据库中是否有该商品的该价格，而价格只有唯一标识id，不适于redis作为key值。
         *
         *
         * 换一种思路后，以商品名和价格组合作为key也是可以使用的，因此使用该方法尝试
         *
         * 这里使用redis的原因：
         * 1.减少对数据库的查询
         * 2.对于商品价格，减少同一段时间的价格的多次插入
         */
        for (CommodityVo commodityVo : commodityVoList) {
            //首先通过商品名称查询redis缓存中是否存在该商品信息
            Commodity commodity = (Commodity) this.redisTemplate.opsForValue().get("commodity:" + commodityVo.getComName());
            //如果不存在，则进去数据库中找
            if (null == commodity) {
                //创建查询条件
                QueryWrapper<Commodity> commodityWrapper = new QueryWrapper<>();
                //写入查询条件,查询数据库中是否有该商品
                commodityWrapper.eq("comName", commodityVo.getComName());
                commodity = commodityMapper.selectOne(commodityWrapper);
            }
            //数据库或缓存中存在该商品，则进入if中，否则不插入价格信息
            if (null != commodity) {
                //在redis中查询价格信息，以商品名称和价格联合作为key，价格信息作为value
                Price price = (Price) this.redisTemplate.opsForValue().get("price:" + commodityVo.getComName() + commodityVo.getPriceNow());

                //如果价格信息为空则进入到数据库中进行插入，即使数据库中存在价格信息，但价格采集时间不同
                if (null == price) {
                    //在commodityVoList中筛选出价格信息插入到数据库中
                    price = new Price();
                    price.setPriceTime(simpleDateFormat.parse(commodityVo.getPriceTime()));
                    price.setPriceNow(commodityVo.getPriceNow());
                    price.setComId(commodity.getComId());
                    //将价格信息插入到数据库中
                    priceMapper.insert(price);
                    //将价格信息插入到redis缓存中
                    this.redisTemplate.opsForValue().set("price:" + commodityVo.getComName() + commodityVo.getPriceNow(), price);
                }else {
                    //redis缓存中存在价格
                    System.out.println("插入失败,同一时间段内不允许多次插入价格到数据库中");
                }
            } else {
                System.out.println("抱歉，商品列表中没有该商品，不能插入价格");
            }
        }


    }
}
