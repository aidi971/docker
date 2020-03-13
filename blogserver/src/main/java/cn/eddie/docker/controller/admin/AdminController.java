package cn.eddie.docker.controller.admin;

import cn.eddie.docker.bean.Article;
import cn.eddie.docker.bean.RespBean;
import cn.eddie.docker.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    ArticleService articleService;

    @RequestMapping(value = "/article/all", method = RequestMethod.GET)
    public Map<String, Object> getArticleByStateByAdmin(
            @RequestParam(value = "page",
                    defaultValue = "1") Integer page,
            @RequestParam(value = "count",
                    defaultValue = "6") Integer count, String keywords) {
        List<Article> articles = articleService.getAricleByState(-2, page, count, keywords);
        Map<String, Object> map = new HashMap<>();
        map.put("articles", articles);
        map.put("totalCount", articleService.getArticleCountByState(1, null, keywords));
        return map;
    }

    @RequestMapping(value = "/article/dustbin", method = RequestMethod.PUT)
    public RespBean updateArticleState(Long[] aids, Integer state) {
        if (articleService.updateArticleState(aids, state) == aids.length) {
            return new RespBean("success", "删除成功");
        }
        return new RespBean("error","删除失败");
    }

}
