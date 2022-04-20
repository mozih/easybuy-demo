package com.mo.easybuy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mo.easybuy.mapper.CommodityMapper;
import com.mo.easybuy.mapper.PriceMapper;
import com.mo.easybuy.pojo.Commodity;
import com.mo.easybuy.pojo.Price;
import com.mo.easybuy.pojo.vo.CommodityVo;
import com.mo.easybuy.service.CommodityService;
import com.mo.easybuy.service.impl.CommodityServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * author mozihao
 * create 2022-03-03 16:07
 * Description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CheckTest {
    @Resource
    private CommodityMapper commodityMapper;
    @Resource
    private PriceMapper priceMapper;
    @Autowired
    private CommodityServiceImpl commodityService;

    @Test
    public void testCrawlMethod(){
        try {
            this.commodityService.crawerCommodity("眼镜");

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void selectTest(){
        CommodityVo commodityVo = new CommodityVo();
        Commodity commodity = commodityMapper.selectById(812);
        commodityVo.setComAddress(commodity.getComAddress());//商品来源
        commodityVo.setComDetail(commodity.getComDetail());//商品详细
        commodityVo.setComUrl(commodity.getComUrl());//商品链接

        QueryWrapper<Price> priceQueryWrapper = new QueryWrapper<>();
        priceQueryWrapper.eq("comId",commodity.getComId());
        List<Price> prices = priceMapper.selectList(priceQueryWrapper);//商品所有价格
        commodityVo.setPriceNow(prices.get(0).getPriceNow());

        System.out.println(commodityVo);//输出商品信息

    }
}
