package com.mo.easybuy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mo.easybuy.mapper.MarkMapper;
import com.mo.easybuy.mapper.PriceMapper;
import com.mo.easybuy.pojo.Commodity;
import com.mo.easybuy.mapper.CommodityMapper;
import com.mo.easybuy.pojo.Mark;
import com.mo.easybuy.pojo.Price;
import com.mo.easybuy.pojo.vo.CommodityVo;
import com.mo.easybuy.service.CommodityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mo.easybuy.utils.crawler.Crawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mo
 * @since 2022-03-03
 */
@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {
    @Resource
    private CommodityMapper commodityMapper;
    @Resource
    private PriceMapper priceMapper;
    @Resource
    private MarkMapper markMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 爬取商品信息进入数据库中
     *
     * @param keyword
     */
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    void crawerCommodity(String keyword) throws ParseException {
        //1.首先使用爬虫类进行数据爬取
        List<CommodityVo> commodityVoList = Crawler.crawler(keyword);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //存储要插入的商品信息，进行统一插入，优化系统性能
        List<Commodity> commodities = new ArrayList<>(commodityVoList.size());

        //for循环出来后commodities数组中就存好了数据库中没有的商品信息
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
                commodity.setComPicUrl(commodityVo.getComPicUrl());

                //第二层保护，先查询数据库中是否有该商品信息，有就不插入，防止商品信息重复
                //创建查询条件
                QueryWrapper<Commodity> wrapper = new QueryWrapper<>();
                //写入查询条件,查询数据库中是否有该商品
                wrapper.eq("comName", commodity.getComName());
                //查询数据库中是否有该商品
                Commodity comTemp = commodityMapper.selectOne(wrapper);
                //如果数据库中没有该商品信息，则插入到数据库中
                if (null == comTemp) {
                    commodities.add(commodity);
//                    commodityMapper.insert(commodity);
                }else {
                    //将数据库中已经存在的商品信息存储到redis中
                    this.redisTemplate.opsForValue().set("commodity:" + commodityVo.getComName(), comTemp);
                }
                //下面的else语句是前期插入时没有插入图片，补充上去的，现在不需要了
//                else {
//                    //将图片插入
//                    String comPicUrl = commodityMapper.selectOne(wrapper).getComPicUrl();
//                    if (null == comPicUrl) {
//                        UpdateWrapper<Commodity> commodityUpdateWrapper = new UpdateWrapper<>();
//                        commodityUpdateWrapper.eq("comName", commodity.getComName()).set("comPicUrl", commodity.getComPicUrl());
//                        commodityMapper.update(null, commodityUpdateWrapper);
//                    }
//                }
            }
        }
        //将商品信息插入到数据库中
        this.commodityMapper.insertBatchSomeColumn(commodities);

        /**
         * 这里使用redis的原因：
         * 1.减少对数据库的查询
         * 2.对于商品价格，减少同一段时间的价格的多次插入
         */
        //优化插入商品价格,使得价格不再一条一条插入
        ArrayList<Price> prices = new ArrayList<>(commodityVoList.size());

        //for循环出来后，prices数组中就存放近期没有更新的价格数据
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

                //如果redis中没有价格信息则插入到数据库中，即使数据库中存在价格信息，但价格采集时间不同
                if (null == price) {
                    //在commodityVoList中筛选出价格信息插入到数据库中
                    price = new Price();
                    price.setPriceTime(simpleDateFormat.parse(commodityVo.getPriceTime()));
                    price.setPriceNow(commodityVo.getPriceNow());
                    price.setComId(commodity.getComId());
                    //将价格信息插入到数据库中
                    prices.add(price);
//                    priceMapper.insert(price);

                    //将价格信息插入到redis缓存中
                    this.redisTemplate.opsForValue().set("price:" + commodityVo.getComName() + commodityVo.getPriceNow(), price);
                }
                //redis缓存中存在价格则不插入
                //else{}
            } else {
                System.out.println("无该商品信息");
            }
        }
        //将商品价格信息插入到数据库中
        this.priceMapper.insertBatchSomeColumn(prices);

    }

    @Override
    public List<CommodityVo> getCommodity(String keyword, String[] shops) {
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //进行数据爬取
        try {
            this.crawerCommodity(keyword);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //如果redis存了商品信息，先从redis中获取商品信息，否则进入数据库中获取，并存到redis中
        //使用keyword作为redis的key值
        List<Commodity> commodityList = null;

        if (null != this.redisTemplate.opsForValue().get("keyword:" + keyword)) {
            commodityList = (List<Commodity>) this.redisTemplate.opsForValue().get("keyword:" + keyword);
        } else {
            //redis中不存在则从数据库中获取
            QueryWrapper<Commodity> commodityQueryWrapper = new QueryWrapper<>();
            commodityQueryWrapper.like("comDetail", keyword);
            commodityList = this.commodityMapper.selectList(commodityQueryWrapper);
            //存入redis中
            this.redisTemplate.opsForValue().set("keyword:" + keyword, commodityList);
        }

        //创建商品信息+价格信息commodityVCo数组
        ArrayList<CommodityVo> commodityVos = new ArrayList<>(commodityList.size());
        //先将商品信息存入数组中
        //筛选出用户勾选的商城的商品
        if (shops == null){
            return commodityVos;
        }
        if (shops.length == 4) {
            for (Commodity commodity : commodityList) {
                CommodityVo commodityVo = new CommodityVo();
//            System.out.println(commodity);
                commodityVo.setComId(commodity.getComId());
                commodityVo.setComName(commodity.getComName());
                commodityVo.setComDetail(commodity.getComDetail());
                commodityVo.setComUrl(commodity.getComUrl());
                commodityVo.setComPicUrl(commodity.getComPicUrl());
                commodityVo.setComMarknumber(commodity.getComMarknumber());
                commodityVo.setComAddress(commodity.getComAddress());
                commodityVo.setComScan(commodity.getComScan());
                commodityVos.add(commodityVo);
            }
        }else {
            for (String s : shops) {
                for (Commodity commodity : commodityList) {
                    if (commodity.getComAddress().indexOf(s) >= 0){
                        CommodityVo commodityVo = new CommodityVo();
//            System.out.println(commodity);
                        commodityVo.setComId(commodity.getComId());
                        commodityVo.setComName(commodity.getComName());
                        commodityVo.setComDetail(commodity.getComDetail());
                        commodityVo.setComUrl(commodity.getComUrl());
                        commodityVo.setComPicUrl(commodity.getComPicUrl());
                        commodityVo.setComMarknumber(commodity.getComMarknumber());
                        commodityVo.setComAddress(commodity.getComAddress());
                        commodityVo.setComScan(commodity.getComScan());
                        commodityVos.add(commodityVo);
                    }
                }
            }
        }

        //查询价格表
        for (int i = 0; i < commodityVos.size(); i++) {
            //查询redis中是否存在价格信息，以商品id作为key
            List<Price> priceList = (List<Price>)this.redisTemplate.opsForValue().get("prices:" + commodityVos.get(i).getComId());
            //为空则从数据库中查询
            if (null == priceList) {
                QueryWrapper<Price> priceQueryWrapper = new QueryWrapper<>();
                priceQueryWrapper.eq("comId", commodityVos.get(i).getComId()).orderByDesc("priceTime");
                priceList = this.priceMapper.selectList(priceQueryWrapper);
                //存入redis中
                this.redisTemplate.opsForValue().set("prices:" + commodityVos.get(i).getComId(), priceList);
            }
            //将价格存到商品信息中
            if (priceList.size() <= 0) {
                commodityVos.get(i).setPriceNow(new BigDecimal(0));
                commodityVos.get(i).setPriceTime(simpleDateFormat.format(new Date()));
            } else {
                commodityVos.get(i).setPriceNow(priceList.get(0).getPriceNow());
                commodityVos.get(i).setPriceTime(simpleDateFormat.format(priceList.get(0).getPriceTime()));
            }
        }
        return commodityVos;
    }

    @Override
    public Commodity getCommodityById(Integer comId) {
        return this.commodityMapper.selectById(comId);
    }

    @Override
    public int updateMarkById(Integer comId) {
        QueryWrapper<Mark> markQueryWrapper = new QueryWrapper<>();
        //获得评价数量
        markQueryWrapper.eq("comId", comId);
        Long comMarkNumber = Long.valueOf(this.markMapper.selectCount(markQueryWrapper));
        //更新到表中
        UpdateWrapper<Commodity> commodityUpdateWrapper = new UpdateWrapper<>();
        commodityUpdateWrapper.eq("comId", comId).set("comMarkNumber", comMarkNumber);

        return this.commodityMapper.update(null, commodityUpdateWrapper);
    }
}
