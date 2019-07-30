package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.List;

public class FTPUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FTPUtil.class);
    private static final String FTP_IP = PropertiesUtil.getProperties("ftp.server.ip");
    private static final String FTP_USER = PropertiesUtil.getProperties("ftp.user");
    private static final String FTP_PASSWORD = PropertiesUtil.getProperties("ftp.pass");

    private String ip;
    private Integer port;
    private String user;
    private String password;
    private FTPClient ftpClient;

    public FTPUtil(String ip, Integer port, String user, String password) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
    }
    public static boolean upLoad(List<File> fileList){
        FTPUtil ftpUtil = new FTPUtil(FTP_IP,21,FTP_USER,FTP_PASSWORD);
        LOGGER.info("开始连接ftp服务器");
        boolean result = ftpUtil.uploadFile("mmall/ftp",fileList);
        LOGGER.info("开始连接ftp服务器,结束上传,上传结果:{}",result);
        return result;
    }

    private boolean uploadFile(String path,List<File> fileList) {
        boolean uploaded = true;
        InputStream inputStream = null;
        if (connection(this.ip,this.port,this.user,this.password)){
            try {
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //不存在创建文件夹
                if (!ftpClient.changeWorkingDirectory(path)) {
                     ftpClient.changeWorkingDirectory("/");
                     String[] dirs = path.split("/");
                     for (String dir : dirs) {
                         ftpClient.makeDirectory(dir);
                         ftpClient.changeWorkingDirectory(dir);
                      }
                }
                ftpClient.enterLocalPassiveMode();// 大部分用在Linux上
                for(File fileItem : fileList){
                    inputStream = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),inputStream);
                }
            } catch (IOException e) {
                LOGGER.error("上传文件异常",e);
                uploaded = false;
                e.printStackTrace();
            }finally {
                try {
                    inputStream.close();
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return uploaded;
    }

    private boolean connection(String ip, Integer port, String user, String password) {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            //连接服务器
            ftpClient.connect(InetAddress.getByName(ip),port);
        } catch (IOException e) {
            LOGGER.error("服务器ip出错",e);
        }
        try {
            //登录服务器
            isSuccess = ftpClient.login(user,password);
        } catch (IOException e) {
            LOGGER.error("用户名或者密码错误",e);
        }
        return isSuccess;
    }
}
