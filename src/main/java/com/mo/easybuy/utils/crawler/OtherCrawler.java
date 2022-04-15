package com.mo.easybuy.utils.crawler;

import com.mo.easybuy.pojo.vo.CommodityVo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * author mozihao
 * create 2022-02-28 14:56
 * Description
 */

public class OtherCrawler implements Callable {
    private String keyword;

    public OtherCrawler(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public List<CommodityVo> call() throws Exception {
        return this.crawl(keyword);
    }

    public List<CommodityVo> crawl(String keyword) throws IOException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Document doc = null;
        //现已不可用
        //168商城获取商品信息url
        String url="https://www.168.com/buy/GoodsSearchForC";
        //商品链接url
        String comUrl = "https://www.168.com/common/GoodsDetail.html?goodsid=";
        //爬取的商品总个数
        int total = 32;
        //list存放爬出来的商品信息
        List<CommodityVo> commodityVoList = new ArrayList<>(total);
        doc = Jsoup.connect(url).data("name",keyword).post();
        System.out.println(doc.html());
        String[] allData = doc.text().split("pageNum");
        String name = "";
        //获取商品名称和商品链接,商品信息在第三个开始，到第34个结束，共32个商品信息
        for (int i = 2; i < 34; i++) {
            //4~9
            CommodityVo commodityVo = new CommodityVo();
            commodityVo.setComAddress("168商城");
            name = allData[i].substring(allData[i].indexOf("id") + 4, allData[i].indexOf("id") + 9);
            commodityVo.setComUrl(comUrl+name);
            commodityVo.setComName(name);
            //将商品存放到list中
            commodityVoList.add(commodityVo);
        }

        //获取商品描述
        String detail="";
        for (int i = 2,j=0; i < 34; i++,j++) {
            detail = allData[i].substring(allData[i].indexOf("goodsName")+12,allData[i].indexOf("minPrice")-3);
            commodityVoList.get(j).setComDetail(detail);
        }

        //获取商品价格
        String price = "";
        for (int i = 2,j=0; i < 34; i++,j++) {
            price = allData[i].substring(allData[i].indexOf("minPrice")+10,allData[i].indexOf("minInPrice")-2);
            commodityVoList.get(j).setPriceNow(new BigDecimal(price));
            commodityVoList.get(j).setPriceTime(simpleDateFormat.format(new Date()));
        }

//        for (String allDatum : allData) {
//            System.out.println(allDatum);
//        }
        //输出商品集合信息
//        for (CommodityVo commodityVo : commodityVoList) {
//            System.out.println(commodityVo);
//        }

        System.out.println("共搜索出" + total + "项");
        return commodityVoList;
    }
}
