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
public class DandanCrawler implements Callable {
    private String keyword;

    public DandanCrawler(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public List<CommodityVo> call() throws Exception {
        return this.crawl(keyword);
    }

    public List<CommodityVo> crawl(String keyword) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Document doc = null;
        //当当url
        String url = "http://search.dangdang.com/?key=" + keyword + "/";

        doc = Jsoup.connect(url).get();
        System.out.println(doc.title());

        //获得ul列表,每个li项即是一个商品
        Elements commodity_list = doc.select("ul[class='bigimg cloth_shoplist']").select("li[ddt-pit]");
        List<CommodityVo> commodityVoList = new ArrayList<>(commodity_list.size());

//        System.out.println(commodity_list.html());
//        System.out.println(doc.html());

        //获取商品描述和链接
        Elements commodity_linkList = commodity_list.select("p[class='name']").select("a");

        for (Element element : commodity_linkList) {
            CommodityVo commodityVo = new CommodityVo();
            commodityVo.setComDetail(element.attr("title"));
            commodityVo.setComUrl(element.attr("abs:href"));
            commodityVo.setComAddress("当当网");
            commodityVoList.add(commodityVo);
//            System.out.println("商品描述：" + element.attr("title"));
//            System.out.println("商品链接" + element.attr("abs:href"));
//            System.out.println("*******************");
        }

        //获取商品名称和图片链接
        for (int i = 0; i < commodity_list.size(); i++) {
            commodityVoList.get(i).setComName(commodity_list.get(i).attr("id"));
            //获取图片链接
            String imgUrl = commodity_list.get(i).select("a[class='pic']").select("img").attr("abs:data-original");
            if ("".equals(imgUrl)) {
                commodityVoList.get(i).setComPicUrl(commodity_list.get(i).select("a[class='pic']").select("img").attr("abs:src"));
//                System.out.println(commodity_list.get(i).select("a[class='pic']").select("img").attr("abs:src"));
            } else {
                commodityVoList.get(i).setComPicUrl(imgUrl);
//                System.out.println(imgUrl);
            }
        }

        //获取商品价格
        Elements commodity_priceList = commodity_list.select("span[class='price_n']");
        for (int i = 0; i < commodity_list.size(); i++) {
            commodityVoList.get(i).setPriceNow(new BigDecimal(commodity_priceList.get(i).text().substring(1)));
//            System.out.println(commodity_priceList.get(i).text().substring(1));
            commodityVoList.get(i).setPriceTime(simpleDateFormat.format(new Date()));
        }

        //输出list内容
//        for (CommodityVo commodityVo : commodityVoList) {
//            System.out.println(commodityVo);
//        }
        System.out.println("共搜索出" + commodity_list.size() + "项");
        return commodityVoList;
    }

}
