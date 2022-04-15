package com.mo.easybuy.utils.crawler;

import com.mo.easybuy.pojo.vo.CommodityVo;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.HttpStatusException;
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
public class AmazonCrawler implements Callable {
    private String keyword;

    public AmazonCrawler(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public List<CommodityVo> call() throws Exception {
        return this.crawl(keyword);
    }

    public List<CommodityVo> crawl(String keyword) throws IOException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Document doc = null;
        //中国亚马逊url
        String url = "https://www.amazon.cn/s?k=" + keyword;
        try {
            doc = Jsoup.connect(url).get();
            System.out.println(doc.title());
        } catch (HttpStatusException e) {
            //捕获到异常，直接返回空的数组
            return new ArrayList<>();
        }
        //获得ul列表,每个li项即是一个商品
        Elements commodity_list = doc.select("div[class='sg-col-4-of-12 s-result-item s-asin sg-col-4-of-16 sg-col s-widget-spacing-small sg-col-4-of-20']");
        List<CommodityVo> commodityVoList = new ArrayList<>(commodity_list.size());

//        System.out.println(commodity_list.html());
//        System.out.println(doc.html());

        //获取商品描述和链接
        Elements commodity_nameList = commodity_list.select("h2").select("a");
        for (Element element : commodity_nameList) {
            CommodityVo commodityVo = new CommodityVo();
            commodityVo.setComDetail(element.text());
            commodityVo.setComAddress("亚马逊");
            commodityVo.setComUrl(element.attr("abs:href"));
            commodityVoList.add(commodityVo);
        }
        String commodityPrice = null;
        //获取商品名称
        for (int i = 0; i < commodity_list.size(); i++) {
            //获取商品名，唯一标识符
            commodityVoList.get(i).setComName(commodity_list.get(i).attr("data-asin"));
            //获取图片链接
            commodityVoList.get(i).setComPicUrl(commodity_list.get(i).select("img[class='s-image']").attr("abs:src"));
//            System.out.println(commodity_list.get(i).select("img[class='s-image']").attr("abs:src"));

            //获取商品价格
            commodityPrice = commodity_list.get(i).select("span[class='a-offscreen']").text();
            try {
                commodityVoList.get(i).setPriceNow(new BigDecimal(commodityPrice.substring(1).replace(",", "")));
            } catch (Exception e) {
                System.out.println("异常跳出" + e);
                commodityVoList.get(i).setPriceNow(new BigDecimal(0));
                //continue;--- 不需要写continue，因为写不写，都会继续循环，不会异常后直接退出的。
            }
            //设置价格采集时间
            commodityVoList.get(i).setPriceTime(simpleDateFormat.format(new Date()));
        }

        //输出list内容
//        for (CommodityVo commodityVo : commodityVoList) {
//            System.out.println(commodityVo);
//        }
        System.out.println("共搜索出" + commodity_list.size() + "项");
        return commodityVoList;
    }

    private  String doGetHtml(String url){
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet(url);
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            //判断状态码是否是200
            if(response.getStatusLine().getStatusCode()==200){
                if(response.getEntity()!=null) {
                    String content = EntityUtils.toString(response.getEntity() ,"utf8");
                    return content;
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
