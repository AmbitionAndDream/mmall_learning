package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 创建本地的Token缓存
 */
public class TokenCache {
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);
    public static final String TOKEN_PREFIX = "token_";
    private static LoadingCache<String,String> loadingCache = CacheBuilder.newBuilder()
            .maximumSize(100) //最大缓存数量，超过按LRU算法清除
            .expireAfterAccess(60,TimeUnit.SECONDS)  //最大存活时间
            .build(new CacheLoader<String, String>() {
                //默认的数据加载实现,当调用get取值的时候,如果key没有对应的值,就调用这个方法进行加载.
                @Override
                public String load(String key) {
                    return "null";
                }
            });
    public static void setKey(String key,String value){
        loadingCache.put(key,value);
    }
    public static String getKey(String key){
        String values ;
        try {
            values = loadingCache.get(key);
            if ("null".equals(values)){
                return null;
            }
            return values;
        } catch (ExecutionException e) {
            logger.error("localCache get error",e);
        }
        return null;
    }
}
