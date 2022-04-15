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
public class SuningCrawler extends BreadthCrawler {

    public SuningCrawler(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
    }

    /**
     * 必须重写 visit 方法，作用是:
     * 在整个抓取过程中,只要抓到符合要求的页面,webCollector 就会回调该方法,并传入一个包含了页面所有信息的 page 对象
     * @param page
     * @param crawlDatums
     */
    @Override
    public void visit(Page page, CrawlDatums crawlDatums) {
        System.out.println("开始爬取----------------");
        if (page.matchUrl("https://search.suning.com/.*")){
            Elements commodity_list = page.select("div[class='res-info']");

            for (Element element : commodity_list) {
                System.out.println(element.html());
            }
        }

    }

    public static void main(String[] args) {

        String keyword = "小米";
        String url="https://search.suning.com/" + keyword + "/";

        SuningCrawler crawl = new SuningCrawler("", true);
        crawl.addSeed(url);
        crawl.addSeed("https://search.suning.com/苹果/");
        crawl.addSeed("https://search.suning.com/小米/");
        crawl.addSeed("https://search.suning.com/鞋子/");
        try {
            crawl.start(4);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            SuningCrawler.crawl(keyword);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public static List<CommodityVo> crawl(String keyword) throws IOException {
        Document doc = null;
        //苏宁易购url
        String url="https://search.suning.com/" + keyword + "/";

        List<CommodityVo> commodityVoList = new ArrayList<>(30);

        doc = Jsoup.connect(url).get();

        System.out.println(doc.title());
        //获得ul列表,每个li项即是一个商品
        Elements commodity_list = doc.select("div[class='res-info']");

//        System.out.println(commodity_list.html());
        System.out.println(doc.html());

        //获取商品名称
        Elements commodity_nameList = commodity_list.select("div[class='title-selling-point']").select("a");

//        for (Element element : commodity_nameList) {
//            System.out.println(element.html());
//        }

        for (Element element : commodity_nameList) {
            CommodityVo commodityVo = new CommodityVo();
            //设商品名称
            commodityVo.setComName(element.text());
            //设商品地址
            commodityVo.setComAddress("苏宁易购");
            //获取商品链接
            commodityVo.setComUrl(element.attr("abs:href"));

            commodityVoList.add(commodityVo);
        }

        //获取商品价格
        Elements commodity_priceList = doc.select("div[class='price-box']");
//        for (Element element : commodity_priceList) {
//            System.out.println(element.html());
//        }


//        for (CommodityVo commodityVo : commodityVoList) {
//            System.out.println(commodityVo);
//        }
        System.out.println("共搜索出" + commodity_list.size() + "项");
        return null;
    }
}
