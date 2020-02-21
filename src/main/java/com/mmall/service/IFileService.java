package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Junrui Gong
 * @Date: 2/21/20
 */

public interface IFileService {

    public String upload(MultipartFile file, String path);
}
