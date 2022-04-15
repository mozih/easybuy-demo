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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * author mozihao
 * create 2022-03-03 14:17
 * Description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestAutoInsert {
    @Resource
    private CommodityMapper commodityMapper;
    @Resource
    private PriceMapper priceMapper;

    @Test
    public void testComInsert(){
        Commodity commodity = new Commodity();
        commodity.setComUrl("xxxxxxxxxxxxx");
        commodity.setComAddress("无商品来源");
        commodity.setComDetail("苹果手机");
        commodity.setComName("3451456136413461346");

        commodityMapper.insert(commodity);
    }

    @Test
    public void insertDataTest() throws ParseException {
        JdCrawler xiaomi = new JdCrawler("xiaomi");
        List<CommodityVo> commodityVoList = Crawler.crawler("苹果");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        //将commodityVoList筛选出商品信息插入到数据库中
        for (CommodityVo commodityVo : commodityVoList) {
            Commodity commodity = new Commodity();
            commodity.setComUrl(commodityVo.getComUrl());
            commodity.setComAddress(commodityVo.getComAddress());
            commodity.setComDetail(commodityVo.getComDetail());
            commodity.setComName(commodityVo.getComName());
            //创建查询条件
            QueryWrapper<Commodity> wrapper = new QueryWrapper<>();
            //写入查询条件,查询数据库中是否有该商品
            wrapper.eq("comName",commodity.getComName());
            //如果数据库中没有该商品信息，则插入到数据库中
            if (null == commodityMapper.selectOne(wrapper)){
                commodityMapper.insert(commodity);
            }
//            System.out.println(commodityMapper.selectOne(commodityQueryWrapper));
        }
        //将commodityVoList筛选出价格信息插入到数据库中
        for (CommodityVo commodityVo : commodityVoList) {

            //创建查询条件
            QueryWrapper<Commodity> commodityWrapper = new QueryWrapper<>();
            //写入查询条件,查询数据库中是否有该商品
            commodityWrapper.eq("comName",commodityVo.getComName());
            Commodity commodity = commodityMapper.selectOne(commodityWrapper);

            if(null != commodity){
                QueryWrapper<Price> priceWrapper = new QueryWrapper<Price>();
                //写入价格，查询相同商品的价格是否相同
                priceWrapper.eq("comId",commodity.getComId()).eq("priceNow",commodityVo.getPriceNow());
                //如果数据库中没有该商品的该价格，则插入
                if (priceMapper.selectList(priceWrapper).size() == 0){
                    Price price = new Price();
                    price.setPriceTime(simpleDateFormat.parse(commodityVo.getPriceTime()));
                    price.setPriceNow(commodityVo.getPriceNow());
                    price.setComId(commodity.getComId());
                    priceMapper.insert(price);
                }else {
                    System.out.println("插入失败,已存在该价格");
                }
            }else {
                System.out.println("抱歉，商品列表中没有该商品，不能插入价格");
            }
        }
    }
}
