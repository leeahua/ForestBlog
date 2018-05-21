package com.liuyanzhao.blog.Interceptor;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.liuyanzhao.blog.entity.Options;
import com.liuyanzhao.blog.entity.custom.*;
import com.liuyanzhao.blog.service.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeResourceInterceptor implements WebRequestInterceptor {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private OptionsService optionsService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final Logger log = LoggerFactory.getLogger(HomeResourceInterceptor.class);
    /**
     * 在请求处理之前执行，该方法主要是用于准备资源数据的，然后可以把它们当做请求属性放到WebRequest中
     */
    @Override
    public void preHandle(WebRequest request) throws Exception {
        System.out.println("HomeResourceInterceptor...preHandle......");
        //导航主要菜单显示
        List<CategoryCustom> categoryList;
        if(redisTemplate.hasKey("categoryList")){
            categoryList = (List<CategoryCustom>)redisTemplate.opsForList().range("categoryList",0,100).get(0);
             log.info("categoryList:{}", JSONObject.valueToString(categoryList));
        }else{
            //分类目录显示
            categoryList = categoryService.listCategory(1);
            redisTemplate.opsForList().leftPush("categoryList",categoryList);
            redisTemplate.expire("categoryList",60*10,TimeUnit.SECONDS);
        }
        //categoryList = categoryService.listCategory(1);

        request.setAttribute("categoryList",categoryList,WebRequest.SCOPE_REQUEST);
        //菜单显示
       /* List<MenuCustom> menuCustomList ;
        if(redisTemplate.hasKey("menuCustomList")){
            menuCustomList = (List<MenuCustom>)redisTemplate.opsForList().leftPop("menuCustomList");

        }else{
            //分类目录显示
            menuCustomList = menuService.listMenu(1);
            redisTemplate.opsForList().leftPush("menuCustomList",menuCustomList);
            redisTemplate.expire("menuCustomList",60*10,TimeUnit.SECONDS);
        }
        log.info("menuList:{}",JSONObject.valueToString(menuCustomList));
        request.setAttribute("menuCustomList",menuCustomList,WebRequest.SCOPE_REQUEST);*/

        //侧边栏显示
        //标签列表显示
		List<TagCustom> tagList;
		if(redisTemplate.hasKey("tagList")){
            tagList  = (List<TagCustom>)redisTemplate.opsForList().range("tagList",0,-1).get(0);
        }else{
            tagList = tagService.listTag(1);
            redisTemplate.opsForList().leftPush("tagList",tagList);
            redisTemplate.expire("tagList",60*10,TimeUnit.SECONDS);
        }
		request.setAttribute("tagList",tagList,WebRequest.SCOPE_REQUEST);
		//获得随机文章
		List<ArticleCustom> randomArticleList = articleService.listRandomArticle(1,8);
		request.setAttribute("randomArticleList",randomArticleList,WebRequest.SCOPE_REQUEST);
		//获得热评文章
		List<ArticleCustom> mostCommentArticleList = articleService.listArticleByCommentCount(1,8);
		request.setAttribute("mostCommentArticleList",mostCommentArticleList,WebRequest.SCOPE_REQUEST);
		//最新评论
        List<CommentListVo> recentCommentList = commentService.listRecentComment(10);
        request.setAttribute("recentCommentList",recentCommentList,WebRequest.SCOPE_REQUEST);

		//获得网站概况
		/*List<String> siteBasicStatistics = new ArrayList<String>();
		siteBasicStatistics.add(articleService.countArticle(1)+"");
		siteBasicStatistics.add(articleService.countArticleComment(1)+"");
		siteBasicStatistics.add(categoryService.countCategory(1)+"");
		siteBasicStatistics.add(tagService.countTag(1)+"");
		siteBasicStatistics.add(linkService.countLink(1)+"");
		siteBasicStatistics.add(articleService.countArticleView(1)+"");
		request.setAttribute("siteBasicStatistics",siteBasicStatistics,WebRequest.SCOPE_REQUEST);*/
		//最后更新的文章
        ArticleCustom lastUpdateArticle = articleService.getLastUpdateArticle();
        request.setAttribute("lastUpdateArticle",lastUpdateArticle,WebRequest.SCOPE_REQUEST);
        //页脚显示
		//博客基本信息显示(Options)
		Options options = optionsService.getOptions();
		request.setAttribute("options", options,WebRequest.SCOPE_REQUEST);

    }

    @Override
    public void postHandle(WebRequest request, ModelMap map) throws Exception {
    }


    @Override
    public void afterCompletion(WebRequest request, Exception exception)
            throws Exception {
    }
}