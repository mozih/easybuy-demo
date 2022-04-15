package com.mo.easybuy.controller;

import com.mo.easybuy.pojo.Commodity;
import com.mo.easybuy.pojo.User;
import com.mo.easybuy.pojo.vo.CommentVo;
import com.mo.easybuy.pojo.vo.PriceVo;
import com.mo.easybuy.service.CommodityService;
import com.mo.easybuy.service.MarkService;
import com.mo.easybuy.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * author mozihao
 * create 2022-03-21 16:06
 * Description
 */
@Controller
public class CommodityController {
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private MarkService markService;

    @RequestMapping("/getContents")
    @ResponseBody
    public List<CommentVo> getContents(Integer page, Integer comId){
//        System.out.println("这是第"+page+"页，商品id为："+comId);
        List<CommentVo> commentVos = this.markService.selectContent(comId, page);
        return commentVos;
    }

    @RequestMapping("/saveContent")
    @ResponseBody
    public boolean saveContent(Integer comId, String markContent, Integer markScore, Model model, HttpSession session){
        System.out.println("评分："+markScore);
        System.out.println("评价内容："+markContent);
        User user = (User) session.getAttribute("user");
        if (this.markService.insertMark(user.getUserId(),comId,markContent,markScore) > 0){
            this.commodityService.updateMarkById(comId);
            System.out.println("评论成功");
            return true;
        }else {
            System.out.println("评价失败");
            return false;
        }
    }
}
