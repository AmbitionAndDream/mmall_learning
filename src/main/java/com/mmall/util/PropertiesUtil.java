package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

public class PropertiesUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);
    private static Properties properties;

    static {
        String fileName = "mmall.properties";
        properties = new Properties();
        try {
            properties.load(new InputStreamReader(Objects.requireNonNull(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName)),"UTF-8"));
        } catch (IOException e) {
            LOGGER.error("读取配置文件异常",e);
        }
    }

    public static String getProperties(String key){
        String property = properties.getProperty(key.trim());
        if (StringUtils.isBlank(property)){
            return null;
        }
        return property;
    }
    public static String getProperties(String key,String defult){
        String property = properties.getProperty(key.trim());
        if (StringUtils.isBlank(property)){
            property = defult;
        }
        return property;
    }
}
