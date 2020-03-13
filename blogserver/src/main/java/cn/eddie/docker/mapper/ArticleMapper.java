package cn.eddie.docker.mapper;


import cn.eddie.docker.bean.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {
    int addNewArticle(Article article);
    int updarteArticle(Article article);
    List<Article> getArticleByState(@Param("state")Integer state,
                                    @Param("start")Integer start,
                                    @Param("count")Integer count,
                                    @Param("uid")Long uid,
                                    @Param("keywords")String keywords);

    int getArticleCountByState(@Param("state") Integer state,
                               @Param("uid")Long uid,
                               @Param("keywords")String keywords);

    int updateArticleState(@Param("aids")Long aids[],
                           @Param("state")Integer state);

    int updateArticleStateById(@Param("articledId")Integer articledId,
                               @Param("state")Integer state);

    int deleteArticleById(@Param("aids") Long[] aids);

    Article getArticleById(Long aid);

    void pvStatisticsPerDay();

    List<String> getCategories(Long uid);

    List<Integer> getDataStatistics(Long uid);


    void pvIncrement(Long aid);
}
