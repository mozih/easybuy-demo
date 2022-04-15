package com.mo.easybuy.utils.crawler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mo.easybuy.pojo.Commodity;
import com.mo.easybuy.pojo.Price;
import com.mo.easybuy.pojo.vo.CommodityVo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * author mozihao
 * create 2022-03-05 20:50
 * Description 实现多线程爬虫
 */
public class Crawler {
    /**
     *
     * @param keyword
     * @return 根据keyword返回商品列表
     */
    public static List<CommodityVo> crawler(String keyword){
        //创建商品list存储爬取出来的所有商品
        List<CommodityVo> commodityVoList = new ArrayList<CommodityVo>(150);

        //创建爬虫类对象
        AmazonCrawler amazonCrawler = new AmazonCrawler(keyword);
        SuningCrawler suningCrawler = new SuningCrawler(keyword);
        JdCrawler jdCrawler = new JdCrawler(keyword);
        DandanCrawler dandanCrawler = new DandanCrawler(keyword);
        //不可用
//        OtherCrawler otherCrawler = new OtherCrawler(keyword);
        //将此Callable接口实现类的对象作为传递到FutureTask构造器中，创建FutureTask的对象
        FutureTask amazomFuture = new FutureTask<List<CommodityVo>>(amazonCrawler);
        FutureTask suningFuture = new FutureTask<List<CommodityVo>>(suningCrawler);
        FutureTask jdFuture = new FutureTask<List<CommodityVo>>(jdCrawler);
        FutureTask dandanFuture = new FutureTask<List<CommodityVo>>(dandanCrawler);
//        FutureTask otherFuture = new FutureTask<List<CommodityVo>>(otherCrawler);
        //将FutureTask的对象作为参数传递到Thread类的构造器中，创建Thread对象，并调用start()
        new Thread(amazomFuture).start();
        new Thread(suningFuture).start();
        new Thread(jdFuture).start();
        new Thread(dandanFuture).start();
//        new Thread(otherFuture).start();
        try {
            commodityVoList.addAll((List<CommodityVo>) amazomFuture.get());
            commodityVoList.addAll((List<CommodityVo>) suningFuture.get());
            commodityVoList.addAll((List<CommodityVo>) jdFuture.get());
            commodityVoList.addAll((List<CommodityVo>) dandanFuture.get());
//            commodityVoList.addAll((List<CommodityVo>) otherFuture.get());
        }catch (InterruptedException e){
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//        for (CommodityVo commodityVo : commodityVoList) {
//            System.out.println(commodityVo);
//        }
        return commodityVoList;
    }
}
