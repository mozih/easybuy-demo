package com.mo.easybuy.utils.crawler;

import org.jsoup.nodes.Document;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * author mozihao
 * create 2022-03-02 16:05
 * Description
 */
public class MyPipeline implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println("----------get page: " + resultItems.getRequest().getUrl());
        Document doc = resultItems.get("doc");
        System.out.println("----------doc.html:" + doc.html());
    }
}
