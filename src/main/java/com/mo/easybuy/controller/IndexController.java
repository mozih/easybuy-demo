package com.mo.easybuy.controller;

import com.mo.easybuy.pojo.Commodity;
import com.mo.easybuy.pojo.Search;
import com.mo.easybuy.pojo.User;
import com.mo.easybuy.pojo.vo.BrowseVo;
import com.mo.easybuy.pojo.vo.CommodityVo;
import com.mo.easybuy.pojo.vo.CommentVo;
import com.mo.easybuy.pojo.vo.PriceVo;
import com.mo.easybuy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * author mozihao
 * create 2022-03-04 17:40
 * Description
 */
@Controller
public class IndexController {
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private SearchService searchService;
    @Autowired
    private BrowseService browseService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private UserService userService;
    @Autowired
    private MarkService markService;

    //前往主页
    @RequestMapping("/index")
    public String toIndex(){
        return "index";
    }

    //获取搜索历史
    @RequestMapping("/getSearch")
    @ResponseBody
    public List<Search> getSearch(Integer userId){
        List<Search> searchList = this.searchService.getSearch(userId);
        return searchList;
    }

    //清除搜索历史关键词
    @RequestMapping("/deleteSearch")
    @ResponseBody
    public boolean deleteSearch(Integer userId,String searchContent){
        if (this.searchService.deleteSearch(userId,searchContent) > 1){
            System.out.println("已删除关键词");
            return true;
        }else {
            return false;
        }
    }

    //根据搜索关键词和商城名获取商品
    @RequestMapping("/getCommodity")
    @ResponseBody
    public List<CommodityVo> getCommodity(String keyword,String checkedShop,HttpSession session){
        //获取搜索的用户信息
        User user = (User) session.getAttribute("user");
        //插入搜索记录
        if (this.searchService.insertSerach(user.getUserId(),keyword) >= 0){
            System.out.println("搜索记录插入成功！");
        }else {
            System.out.println("搜索记录插入失败！");
        }
        List<CommodityVo> commodityVos = null;
        if (!checkedShop.equals("")){
            String[] shops = checkedShop.split(",");
            commodityVos = this.commodityService.getCommodity(keyword,shops);
        }
//        System.out.println("商品列表长度"+commodityVos.size());
        return commodityVos;
    }

    //前往商品详情页面
    @RequestMapping("/toDetail")
    public String toDetail(Integer comId, Model model,HttpSession session){
        User user = (User) session.getAttribute("user");
        if (this.browseService.saveBrowse(comId,user.getUserId()) >= 1){
            System.out.println("浏览历史存储成功");
        }
        //获得商品信息
        Commodity commodity = this.commodityService.getCommodityById(comId);
        //获得价格列表
        List<PriceVo> priceVoList = this.priceService.getPrices(comId);
        //将信息存入model域中
        model.addAttribute("commodity",commodity);
        model.addAttribute("prices",priceVoList);

        return "commodityDetail";
    }

    //前往浏览历史界面
    @RequestMapping("/toHistory")
    public String toHistory(HttpSession session,Model model){
        User user = (User) session.getAttribute("user");

        //获得浏览历史列表
        List<BrowseVo> browseList = this.browseService.getBrowseList(user.getUserId());
        //以时间为key存放浏览历史
        Map<String, List<BrowseVo>> browseMap = new LinkedHashMap<>();
        for (BrowseVo browseVo : browseList) {
            //若没有这个时间key则new一个浏览对象的数组put进去
            if (browseMap.get(browseVo.getBrowseTime()) == null){
                ArrayList<BrowseVo> browseVos = new ArrayList<>();
                browseVos.add(browseVo);
                browseMap.put(browseVo.getBrowseTime(),browseVos);
            }else {
                //存在这个时间key,将浏览记录放在同一天的浏览List中
                browseMap.get(browseVo.getBrowseTime()).add(browseVo);
            }
        }
        //存放到model域中
        model.addAttribute("browseMap",browseMap);
        Set<Map.Entry<String, List<BrowseVo>>> browseSet = browseMap.entrySet();
//        for (Map.Entry<String, List<BrowseVo>> entry : browseSet) {
//            System.out.println(entry);
//        }
        return "browseHistory";
    }
//********************个人主页控制层***************************
    //前往个人主页
    @RequestMapping("/toUser")
    public String toUser(HttpSession session,Model model){
        User user = (User) session.getAttribute("user");
        //获取用户评论列表
        List<CommentVo> commentVos = this.markService.selctContentByUserId(user.getUserId());
        model.addAttribute("comments", commentVos);
        return "userDetail";
    }

    //更新密码
    @RequestMapping("/updatePwd")
    public String updatePwd(HttpSession session,Model model,String password){
        User user = (User) session.getAttribute("user");
        System.out.println(password);
        user.setUserPassword(password);
        if (userService.updateUserByEmail(user) >=1){
            model.addAttribute("errorMsg", "修改密码成功");
            return "successPage";
        }else {
            model.addAttribute("errorMsg", "修改密码失败");
            return "successPage";
        }
    }

    //删除评论
    @RequestMapping("/deleteMark")
    public String deleteMark(Integer markId){
        this.markService.deleteMark(markId);
            return "userDetail";
    }
}
