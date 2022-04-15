package com.mo.easybuy.utils.crawler;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.rocks.BreadthCrawler;
import com.mo.easybuy.pojo.vo.CommodityVo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * author mozihao
 * create 2022-02-28 14:56
 * Description
 */
public class TianmaoCrawler{

    public static void main(String[] args) {

        String keyword = "小米";

        try {
            TianmaoCrawler.crawl(keyword);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<CommodityVo> crawl(String keyword) throws IOException {
        Document doc = null;
        //天猫url
        String url="https://list.tmall.com/search_product.htm?q=" + keyword;

        List<CommodityVo> commodityVoList = new ArrayList<>(30);

        doc = Jsoup.connect(url).get();

        System.out.println(doc.title());
        //获得ul列表,每个li项即是一个商品
        Elements commodity_list = doc.select("div[class='res-info']");

//        System.out.println(commodity_list.html());
        System.out.println(doc.html());


        System.out.println("共搜索出" + commodity_list.size() + "项");
        return null;
    }
}
