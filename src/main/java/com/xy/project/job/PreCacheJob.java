package com.xy.project.job;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xy.project.model.domain.User;
import com.xy.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存预热任务
 */
@Component
@Slf4j
public class PreCacheJob {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Resource
    private RedissonClient redissonClient;
    private List<Long> mainUserList= Arrays.asList(1L);

    @Scheduled(cron = "0 48 20 * * *")
    public void doCacheRecommendUser(){
        RLock lock = redissonClient.getLock("yupao:precachejob:docache:lock");
        try {
            if (lock.tryLock(0L,30000,TimeUnit.MILLISECONDS)){
                for (Long userId:mainUserList){
                    String redisKey=String.format("yupao:user:recommend:%s",userId);
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1,20),queryWrapper);

                    try {
                        redisTemplate.opsForValue().set(redisKey,userPage,30000L, TimeUnit.MILLISECONDS);
                    }catch (Exception e){
                        log.error("redis set key error");
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //只能释放自己的锁
            if (lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }

}
