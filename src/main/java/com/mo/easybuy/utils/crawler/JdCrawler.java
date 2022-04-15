package com.mo.easybuy.utils.crawler;

import com.mo.easybuy.pojo.Commodity;
import com.mo.easybuy.pojo.Price;
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
 * create 2022-02-28 14:54
 * Description
 */
public class JdCrawler implements Callable {
    private String keyword;

    public JdCrawler(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public List<CommodityVo> call() throws Exception {
        return this.crawl(keyword);
    }

    /**
     * 进行爬取京东商城的具体方法
     *
     * @param keyword
     * @return List<Commodity>
     */
    public List<CommodityVo> crawl(String keyword) throws IOException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Document doc = null;
        //搜索页码,默认第一页
        Integer page = 1;

        //京东搜索url
        String url = "https://search.jd.com/Search?keyword=" + keyword + "&page=" + page;
        //根据url获取到爬取的页面文档
        doc = Jsoup.connect(url).get();
        System.out.println(doc.title());

        //获取到所有搜索出来的商品项
        Elements commodity_list = doc.getElementsByClass("gl-item");
        //商品列表,京东一页只能爬取30项商品
        List<CommodityVo> commodityVoList = new ArrayList<>(commodity_list.size());

        //获取商品名称
//        Elements commodity_nameList = commodity_list.select("div[class='p-name p-name-type-2']").select("em");

        for (Element element : commodity_list) {
            CommodityVo commodityVo = new CommodityVo();
            //获取商品名称
            commodityVo.setComDetail(element.select("div[class='p-name p-name-type-2']").select("em").text());
            commodityVo.setComAddress("京东商城（自营）");
            commodityVo.setComName(element.attr("data-sku"));
            //获取商品链接和图片链接
            commodityVo.setComUrl(element.select("div[class='p-img']").select("a").attr("abs:href"));
            commodityVo.setComPicUrl(element.select("div[class='p-img']").select("img").attr("abs:data-lazy-img"));

            commodityVoList.add(commodityVo);
        }

//        for (Element element : commodity_nameList) {
//            CommodityVo commodityVo = new CommodityVo();
//            commodityVo.setComDetail(element.text());
//            commodityVo.setComAddress("京东商城（自营）");
//            commodityVo.setComName(element.attr("data-sku"));
//            commodityVoList.add(commodityVo);
//        }

//        for (int i = 0; i < commodity_nameList.size(); i++) {
//                System.out.println("第" + page + "页第" + i + "项商品的名称为：" + commodity_nameList.get(i).text());
//        }
//        Elements commodity_nameList = commodity_list.select("em");
//        String[] commodityName = commodity_nameList.text().split("\\￥");
//        for (String name : commodityName) {
//            if (name.equals(" ")){
//                continue;
//            }else {
//                //System.out.println(name.trim());
//            }
//        }
        //获取商品链接和图片链接
//        Elements commodity_linkList = commodity_list.select("div[class='p-img']").select("a");
//
//        for (int i = 0; i < commodity_linkList.size(); i++) {
//            //获取商品链接
//            commodityVoList.get(i).setComUrl(commodity_linkList.get(i).attr("abs:href"));
////                System.out.println("第" + page + "页第" + i + "项商品的链接为：" + commodity_linkList.get(i).attr("abs:href"));
//            //获取图片链接
//            commodityVoList.get(i).setComPicUrl(commodity_linkList.get(i).select("img").attr("abs:data-lazy-img"));
////            System.out.println(commodity_linkList.get(i).select("img").attr("abs:data-lazy-img"));
//        }

        //获取商品价格
        Elements commodity_priceList = commodity_list.select("div[class='p-price']").select("i");
        int count = 0;//计算共爬了多少项商品
        for (int i = 0; i < commodity_priceList.size(); i++) {
            if (commodity_priceList.get(i).text().equals("")) {
                continue;
            } else {
                commodityVoList.get(count).setPriceNow(new BigDecimal(commodity_priceList.get(i).text()));
                commodityVoList.get(count).setPriceTime(simpleDateFormat.format(new Date()));
//                    System.out.println("第" + count + "件商品的价格为：" + commodity_priceList.get(i).text());
                count++;
            }
        }
//        for (Element price : commodity_priceList) {
//            if (price.text().equals("")) {
//                continue;
//            } else {
//                System.out.println("第" + count + "件商品的价格为：" + price.text());
//                count++;
//            }
//        }

//        System.out.println("商品名称列表：" + commodity_name_list.text());

//        System.out.println(commodity_list.html());
//        System.out.println("共搜索出" + commodity_list.size() + "项");

//        for (CommodityVo commodityVo : commodityVoList) {
//            System.out.println("集合中返回的每一项数据：" + commodityVo);
//        }
//        System.out.println(commodityVoList.size());
//        System.out.println(commodity_priceList.size());
//        System.out.println("共搜索出" + count + "项");
        return commodityVoList;
    }


}
