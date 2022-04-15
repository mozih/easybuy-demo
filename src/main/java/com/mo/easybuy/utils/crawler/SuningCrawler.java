package com.mo.easybuy.utils.crawler;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.rocks.BreadthCrawler;
import com.mo.easybuy.pojo.vo.CommodityVo;
import org.jsoup.Connection;
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
public class SuningCrawler implements Callable {
    private String keyword;

    public SuningCrawler(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public List<CommodityVo> call() throws Exception {
        return this.crawl(keyword);
    }

    private String spellUrl(List<CommodityVo> commodityVoList,int count){
        //获取商品价格
        String comName = null;

        //返回价格json的url
        StringBuilder priceUrl = new StringBuilder(1000);
        priceUrl.append("https://ds.suning.com/ds/generalForTile/");

        //首先拼接出获取到商品价格的url,只能先获取到前15个
        for (int i = 0; i < 15; i++) {
            comName = commodityVoList.get(count).getComName();
            if (comName.startsWith("0000000")){
                priceUrl.append(comName.substring(11)+",");
//                System.out.println(comName.substring(11));
            }else {
//                System.out.println(comName.substring(11) + "__2_" + comName.substring(0, 10));
                priceUrl.append(comName.substring(11)+"__2_"+comName.substring(0,10)+",");
            }
            count++;
        }
        //去掉最后一个逗号
        priceUrl.deleteCharAt(priceUrl.length()-1);
        priceUrl.append("-020-2-0000000000-1--.jsonp");
//        System.out.println(priceUrl);
        return priceUrl.toString();
    }

    public List<CommodityVo> crawl(String keyword) throws IOException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Document doc = null;
        //苏宁易购url
        String url="https://search.suning.com/" + keyword + "/";

        doc = Jsoup.connect(url).get();
        System.out.println(doc.title());
        //获得ul列表,每个li项即是一个商品
        Elements commodity_list = doc.select("ul[class='general clearfix']").select("li");
        List<CommodityVo> commodityVoList = new ArrayList<>(commodity_list.size());
//        System.out.println(commodity_list.html());
//        System.out.println(doc.html());

        //获取商品描述
        Elements commodity_nameList = commodity_list.select("div[class='title-selling-point']").select("a");

//        for (Element element : commodity_nameList) {
//            System.out.println(element.html());
//        }

        for (Element element : commodity_nameList) {
            CommodityVo commodityVo = new CommodityVo();
            //设商品描述
            commodityVo.setComDetail(element.text());
            //设商品地址
            commodityVo.setComAddress("苏宁易购");
            //获取商品链接
            commodityVo.setComUrl(element.attr("abs:href"));
            commodityVoList.add(commodityVo);
        }
        //设商品名称和图片链接
        for (int i = 0; i < commodity_list.size(); i++) {
            commodityVoList.get(i).setComName(commodity_list.get(i).attr("id"));
            //获取图片链接
//            System.out.println(commodity_list.get(i).select("div[class='img-block']").select("img").attr("abs:src"));
            commodityVoList.get(i).setComPicUrl(commodity_list.get(i).select("div[class='img-block']").select("img").attr("abs:src"));
        }

        //记录获取了多少个商品的价格
        int count = 0;
        while (count < 30){
            System.out.println("现在的count为：" + count);
            //获取到15个商品的价格的url
            String priceUrl = this.spellUrl(commodityVoList,count);
            //根据url使用jsoup获取到json数据
            Connection.Response response = Jsoup.connect(priceUrl).header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                    .timeout(10000).ignoreContentType(true).execute();
            System.out.println("json的url为："+priceUrl);
            //获取响应内容
            String body = response.body();
            String[] splitList = body.split("price");
            //循环筛选出价格并保存到商品列表中
            for (String s : splitList) {
                //有价格数据的为 ":"158.00","
                if (s.startsWith("\":\"")){
                    //处理没有价格数据的为 ":""," 将值设为0
                    if ("\":\"\",\"".equals(s)){
                        commodityVoList.get(count).setPriceNow(new BigDecimal(0));
                    }else if(s.indexOf("待发布") > 0){
                        commodityVoList.get(count).setPriceNow(new BigDecimal(0));
                    } else{
                        commodityVoList.get(count).setPriceNow(new BigDecimal(s.substring(3,s.length()-3)));
                    }
                    commodityVoList.get(count).setPriceTime(simpleDateFormat.format(new Date()));
                    count++;
                }
            }
        }

        //输出商品集合信息
//        for (CommodityVo commodityVo : commodityVoList) {
//            System.out.println(commodityVo);
//        }
        System.out.println("共搜索出" + commodity_list.size() + "项");
        return commodityVoList;
    }

}
