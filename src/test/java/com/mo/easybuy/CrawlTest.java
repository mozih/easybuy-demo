package com.mo.easybuy;

import com.mo.easybuy.pojo.vo.CommodityVo;
import com.mo.easybuy.utils.crawler.*;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * author mozihao
 * create 2022-03-03 12:26
 * Description
 */
public class CrawlTest {
//    //1.实例化ReentrantLock
//    private ReentrantLock lock = new ReentrantLock();

    //测试京东爬虫
    @Test
    public void jd(){
        JdCrawler jdCrawler = new JdCrawler("鞋子");
        try {
            List<CommodityVo> commodityVoList = jdCrawler.crawl("充电宝");
            for (CommodityVo commodityVo : commodityVoList) {
                System.out.println(commodityVo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //测试亚马逊爬虫
    @Test
    public void amazon(){
        AmazonCrawler amazonCrawler = new AmazonCrawler("鞋子");
        try {
            List<CommodityVo> commodityVoList = amazonCrawler.crawl("小米");

            for (CommodityVo commodityVo : commodityVoList) {
                System.out.println(commodityVo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //测试当当爬虫
    @Test
    public void dangdang(){
        DandanCrawler dangdCrawler = new DandanCrawler("鞋子");
        try {
            List<CommodityVo> commodityVoList = dangdCrawler.crawl("鞋子");
            for (CommodityVo commodityVo : commodityVoList) {
                System.out.println(commodityVo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //测试168商城爬虫
    //不可用
//    @Test
    public void otherTest(){
        OtherCrawler otherCrawler = new OtherCrawler("鞋子");
        try {
            List<CommodityVo> commodityVoList = otherCrawler.crawl("鞋子");
            for (CommodityVo commodityVo : commodityVoList) {
                System.out.println(commodityVo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //测试苏宁爬虫
    @Test
    public void sunningTest(){
        SuningCrawler suningCrawler = new SuningCrawler("衣服");
        try {
            List<CommodityVo> commodityVoList = suningCrawler.crawl("衣服");
            for (CommodityVo commodityVo : commodityVoList) {
                System.out.println(commodityVo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAllCrawl(){
        List<CommodityVo> commodityVoList = new ArrayList<>(150);
        JdCrawler jdCrawler = new JdCrawler("鞋子");
        AmazonCrawler amazonCrawler = new AmazonCrawler("鞋子");
        DandanCrawler dangdCrawler = new DandanCrawler("鞋子");
        SuningCrawler suningCrawler = new SuningCrawler("鞋子");
        try {
            commodityVoList.addAll(jdCrawler.crawl("鞋子"));
            commodityVoList.addAll(amazonCrawler.crawl("鞋子"));
            commodityVoList.addAll(dangdCrawler.crawl("鞋子"));
            commodityVoList.addAll(suningCrawler.crawl("鞋子"));
            for (CommodityVo commodityVo : commodityVoList) {
                System.out.println(commodityVo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrawler(){
        List<CommodityVo> commodityVoList =  Crawler.crawler("鞋子");
        for (CommodityVo commodityVo : commodityVoList) {
            System.out.println(commodityVo);
        }
    }

    @Test
    public void testCrawl(){
        //多线程爬虫测试
        String keyword = "小米";

        List<CommodityVo> commodityVoList = new ArrayList<CommodityVo>(150);

        //创建爬虫类对象
        AmazonCrawler amazonCrawler = new AmazonCrawler(keyword);
        SuningCrawler suningCrawler = new SuningCrawler(keyword);
        JdCrawler jdCrawler = new JdCrawler(keyword);
        DandanCrawler dandanCrawler = new DandanCrawler(keyword);
        OtherCrawler otherCrawler = new OtherCrawler(keyword);

        //将此Callable接口实现类的对象作为传递到FutureTask构造器中，创建FutureTask的对象
        FutureTask amazomFuture = new FutureTask<List<CommodityVo>>(amazonCrawler);
        FutureTask suningFuture = new FutureTask<List<CommodityVo>>(suningCrawler);
        FutureTask jdFuture = new FutureTask<List<CommodityVo>>(jdCrawler);
        FutureTask dandanFuture = new FutureTask<List<CommodityVo>>(dandanCrawler);
        FutureTask otherFuture = new FutureTask<List<CommodityVo>>(otherCrawler);
        //将FutureTask的对象作为参数传递到Thread类的构造器中，创建Thread对象，并调用start()

//        Thread amazomThread = new Thread(amazomFuture);
//        Thread suningThread = new Thread(suningFuture);
//        Thread jdThread = new Thread(jdFuture);
//        Thread dandanThread = new Thread(dandanFuture);
//        Thread otherThread = new Thread(otherFuture);
//        try {
//            //启动爬虫线程，并加入到主线程，主线程等待爬虫线程结束后才开始运行
//            amazomThread.start();
//            amazomThread.join();
//            suningThread.start();
//            suningThread.join();
//            jdThread.start();
//            jdThread.join();
//            dandanThread.start();
//            dandanThread.join();
//            otherThread.start();
//            otherThread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        new Thread(amazomFuture).start();
        new Thread(suningFuture).start();
        new Thread(jdFuture).start();
        new Thread(dandanFuture).start();
        new Thread(otherFuture).start();


        try {
            //future.get()方法会产生阻塞，当任务执行结束后才会返回值
//            List<CommodityVo> amazonCommodity = (List<CommodityVo>) amazomFuture.get();
//            List<CommodityVo> suningCommodity = (List<CommodityVo>) suningFuture.get();
//            List<CommodityVo> jdCommodity = (List<CommodityVo>) jdFuture.get();
//            List<CommodityVo> dandanCommodity = (List<CommodityVo>) dandanFuture.get();
//            List<CommodityVo> otherCommodity = (List<CommodityVo>) otherFuture.get();

            commodityVoList.addAll((List<CommodityVo>) amazomFuture.get());
            commodityVoList.addAll((List<CommodityVo>) suningFuture.get());
            commodityVoList.addAll((List<CommodityVo>) jdFuture.get());
            commodityVoList.addAll((List<CommodityVo>) dandanFuture.get());
            commodityVoList.addAll((List<CommodityVo>) otherFuture.get());
            int count = 0;
            for (CommodityVo commodityVo : commodityVoList) {
                System.out.println(count + "" + commodityVo);
                count++;
            }

//            //2.调用锁定方法lock()
//            lock.lock();
//            System.out.println(amazonCommodity.toString());
//            System.out.println(suningCommodity.toString());
//            System.out.println(jdCommodity.toString());
//            System.out.println(dandanCommodity.toString());
//            System.out.println(otherCommodity.toString());
//            lock.unlock();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
