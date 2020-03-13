package cn.eddie.docker.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataStaticsComponent {
    @Autowired
    ArticleService articleService;

    @Scheduled(cron = "1 0 0 * * ？")
    public void pvStatisticsPerDay(){
        articleService.pvStatisticsPerDay();
    }
}
