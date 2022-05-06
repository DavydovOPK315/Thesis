package com.my.thesis.service;

import com.my.thesis.model.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image uploadImage(MultipartFile multipartImage);

    String downloadImage(Image image);
}
