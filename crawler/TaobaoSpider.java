package com.mo.easybuy.utils.crawler;

import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Scheduled;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TaobaoSpider implements PageProcessor {
	private String targetUrl = "https://search.suning.com/小米/";
	MyPipeline myPipeline = new MyPipeline();

	@Override
	public void process(Page page) {
		String url = page.getRequest().getUrl();
		crawlProductInfo(page);
	}
	/**
	 * 根据搜索页爬取商品信息
	 * @param page
	 * @throws Exception 
	 */
    public void crawlProductInfo(Page page){
       System.out.println("爬取页面："+page.getRequest().getUrl());

//		System.out.println(page.getHtml());
	   Document doc = page.getHtml().getDocument();

	   page.putField("doc",doc);
    }
	Site site = Site.me().setRetryTimes(3).setSleepTime(100)
			.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36")
//			.addHeader("Cookie", "miid=1417462415476410157; cna=IkuKFeYlnAsCAXWVCioV0Fjt; thw=cn; hng=CN%7Czh-CN%7CCNY%7C156; _samesite_flag_=true; cookie2=13d8cbc6794b7e47a56592ae67d1dec0; t=d288403f3469a9331c9c4f26faa6bf88; _tb_token_=ee67ee4feeb0e; v=0; lLtC1_=1; uc3=vt3=F8dBxGR2VY03%2FA9IpE4%3D&id2=Uoe8jRgLdJ2DyA%3D%3D&lg2=VFC%2FuZ9ayeYq2g%3D%3D&nk2=AQYI6HgIuCbHgA0JfA%3D%3D; csg=63e4ad9e; lgc=bb1107382263a; dnk=bb1107382263a; skt=9b0c390505f07ce5; existShop=MTU4NzUzNjgyNg%3D%3D; uc4=nk4=0%40A6tZbQqhFIPrBN3cupikuxnZEeto2fwC&id4=0%40UO%2B6a2sbkQ85%2F7l%2B1KGK0aRpk2I3; tracknick=bb1107382263a; _cc_=URm48syIZQ%3D%3D; sgcookie=E9kd1qgyMSvOk%2Bx66dMhM; enc=qWlpSKNoErXLQgXINnprEUx5fuwUVtb%2FL7HL%2BQN4DB1oAAZRUZVSWEiSZChr7X6%2BgxZWbQ8BLqzm5oQSIk1dLw%3D%3D; tfstk=cTTOBRx64vDGuZON4EnhlQ3fHyklapoOnvXzkjwKs1NzUc0i7sqyrU8cmyf8Bwhd.; tk_trace=oTRxOWSBNwn9dPyorMJE%2FoPdY8zfvmw%2Fq5hp2qIVuPhvyALIyVLcmqfph6ry163J87EhemlAnu%2FxIqCx7OCxzAFskfDTBqWHFNDH4Fqdu7Grucn%2Bjpo63%2FOZHSNyMKbI8KRdYFjbFRz4qdOYiLm%2BrTK1ZTAbtr%2BrXX3cgu%2Bb2pGhRQyupV8NeaFrkbnEZ9dH9w%2Bs25zPSv%2BnVhk87GTBIJ2ksl849fCByHUTwoD%2FAnAwGbMVgKmhDYXJ6NRMSV2oXGGxCiyz3D%2BUBoZUXEdG17OW%2BgiHCw%3D%3D; mt=ci=-1_0; alitrackid=www.taobao.com; lastalitrackid=www.taobao.com; JSESSIONID=0C9DF44D9D98791BD78FA0BAE794D320; uc1=cookie16=Vq8l%2BKCLySLZMFWHxqs8fwqnEw%3D%3D&cookie21=UtASsssmeW6lpyd%2BB%2B3t&cookie15=Vq8l%2BKCLz3%2F65A%3D%3D&existShop=false&pas=0&cookie14=UoTUPcqcAE7spQ%3D%3D; l=eBx3Yo9qqma5YveCBOfwdAkI1zQ9jIRYou8wRckkiT5P9aCp5j25WZjYd8T9CnGVh65DR3kRxbhvBeYBqIv4n5U62j-la_Mmn; isg=BO_vs5GS7cYwwOrW1UPBRcWzfgP5lEO2tFO0RQF8j95lUA9SCWUmBioC1kDuLhsu")
//			.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
//			.addCookie("jiaowu.sicau.edu.cn", "ASPSESSIONIDCSRARDRA", "MBOKABCAEKMOPGBBLMLACNJM")
			//.addCookie("jiaowu.sicau.edu.cn", "ASPSESSIONIDCSRARDRA", "MBOKABCAEKMOPGBBLMLACNJM")
			;
	@Override
	public Site getSite() {
		return site;
	}

	@Scheduled(fixedRate=1000*60*60*5,initialDelay = 1000*1)
	public void taobaoSpiderRun() {
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println("========任务执行时间："+time);
		Spider.create(new TaobaoSpider())
		        .addUrl(targetUrl)
				.addPipeline(myPipeline)
				.thread(1)
				.run();
	}

	public static void main(String[] args) {
		TaobaoSpider taobaoSpider = new TaobaoSpider();
		taobaoSpider.taobaoSpiderRun();
	}
}
