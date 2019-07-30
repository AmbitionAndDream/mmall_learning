package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

public interface FTPService {
    String upload(MultipartFile file, String path);
}
