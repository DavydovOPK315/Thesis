package com.my.thesis.service;

import com.my.thesis.model.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image uploadImage(MultipartFile multipartImage);

    String downloadImage(Image image);

    Image findById(Long id);

    Image findByContent(byte[] content);
}
