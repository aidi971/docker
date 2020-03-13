package cn.eddie.docker.service;

import cn.eddie.docker.bean.Article;
import cn.eddie.docker.mapper.ArticleMapper;
import cn.eddie.docker.mapper.TagsMapper;
import cn.eddie.docker.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.rmi.activation.ActivationID;
import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class ArticleService {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    TagsMapper tagsMapper;

    public int addNewArticle(Article article) {
        //文章截取
        if (article.getSummary() == null || "".equals(article.getSummary())) {
            //直接截取摘要
            String stripHtml = stripHtml(article.getHtmlContent());
            article.setSummary(stripHtml.substring(0, stripHtml.length() > 50 ? 50 : stripHtml.length()));
        }
        if (article.getId() == -1) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (article.getState() == 1) {
                article.setPublishDate(timestamp);
            }
            article.setEditTime(timestamp);
            //添加用户id
            article.setUid(Util.getCurrentUser().getId());
            int i = articleMapper.addNewArticle(article);
            //添加标签
            String[] dynamicTags = article.getDynamiTags();
            if (dynamicTags != null && dynamicTags.length > 0) {
                int tags = addTagsToArticle(dynamicTags, article.getId());
                if (tags == -1) {
                    return tags;
                }
            }
            return i;
        } else {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (article.getState() == 1) {
                //设置发表日期
                article.setPublishDate(timestamp);
            }
            //更新
            article.setEditTime(new Timestamp(System.currentTimeMillis()));
            int i = articleMapper.updarteArticle(article);
            String[] dynameicTags = article.getDynamiTags();
            if (dynameicTags != null && dynameicTags.length > 0) {
                int tags = addTagsToArticle(dynameicTags, article.getId());
                if (tags == -1) {
                    return tags;
                }
            }
            return i;
        }
    }

    private int addTagsToArticle(String[] dynamicTags, long aid) {
        //删除该文章目前所有的标签
        tagsMapper.deleteTagsByAid(aid);
        //把新标签存入数据库
        tagsMapper.saveTags(dynamicTags);
        //查询标签的id
        List<Long> tIds = tagsMapper.getTagsIdByTagName(dynamicTags);
        //更新文章标签
        int i = tagsMapper.saveTags2ArticleTags(tIds, aid);
        return i == dynamicTags.length ? i : -1;
    }


    public String stripHtml(String content) {
        content = content.replaceAll("<p .*?>", "");
        content = content.replaceAll("<br\\s*/?>", "");
        content = content.replaceAll("\\<.*?>", "");
        return content;
    }

    public List<Article> getAricleByState(Integer state, Integer page, Integer count, String keywords) {
        int start = (page - 1) * count;
        Long uid = Util.getCurrentUser().getId();
        return articleMapper.getArticleByState(state, start, count, uid, keywords);
    }

    public int getArticleCountByState(Integer state, Long uid, String keywords) {
        return articleMapper.getArticleCountByState(state, uid, keywords);
    }

    public int updateArticleState(Long[] aids, Integer state) {
        if(state==2){
            return articleMapper.deleteArticleById(aids);
        }else{
            return articleMapper.updateArticleState(aids,2);
        }
    }
    public int restoreArticle(Integer articleId){
        return articleMapper.updateArticleStateById(articleId,1);
    }

    public Article getAticleById(Long aid){
        Article article = articleMapper.getArticleById(aid);
        articleMapper.pvIncrement(aid);
        return article;
    }

    public void pvStatisticsPerDay(){
        articleMapper.pvStatisticsPerDay();
    }

    /**
     * 获取最近七天的日期
     * @return
     */
    public List<String> getCategoies(){
        return articleMapper.getCategories(Util.getCurrentUser().getId());
    }

    /**
     * 获取最近七天的数据
     * @return
     */
    public List<Integer> getDataStatistics(){
        return articleMapper.getDataStatistics(Util.getCurrentUser().getId());
    }

}
