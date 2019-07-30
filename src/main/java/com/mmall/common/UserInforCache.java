package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class UserInforCache {
    @Autowired
    private  UserMapper userMapper;

    private static Logger logger = LoggerFactory.getLogger(UserInforCache.class);

    private LoadingCache<Integer, User> loadingCache = CacheBuilder.newBuilder()
            .expireAfterAccess(2, TimeUnit.MINUTES)
            .maximumSize(1)
            .build(new CacheLoader<Integer, User>() {
                @Override
                public User load(Integer key){
                    logger.info("从数据库中查找");
                    User user = userMapper.selectByPrimaryKey(key);
                    logger.info("找到此人");
                    return user;
                }
            });
    public void setValues(Integer key,User user){
        logger.info("存入缓存中");
        loadingCache.put(key,user);
        logger.info("已存缓存中");
    }
    public User getValues(Integer key){
        User user = null;
        try {
            logger.info("从缓存中获取");
            user = loadingCache.get(key);
            logger.info("已从缓存中获取");
        } catch (ExecutionException e) {
            logger.error("localCache get error",e);
        }
        return user;
    }
}
