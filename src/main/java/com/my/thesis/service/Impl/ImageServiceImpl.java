package com.my.thesis.service.Impl;

import com.my.thesis.model.Image;
import com.my.thesis.repository.ImageDbRepository;
import com.my.thesis.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Autowired
    ImageDbRepository imageDbRepository;

    @Override
    public Image uploadImage(MultipartFile multipartImage) {

        Image dbImage = new Image();
        dbImage.setName(multipartImage.getName());
        try {
            dbImage.setContent(multipartImage.getBytes());
        } catch (IOException e) {
            log.warn("IN uploadImage - no image found {}", multipartImage);
        }

//            imageDbRepository.save(dbImage);


        return dbImage;
    }

    @Override
    public String downloadImage(Image image) {

        byte[] byteData = image.getContent();

        return Base64.getMimeEncoder().encodeToString(byteData);
    }

    @Override
    public Image findById(Long id) {
        return imageDbRepository.findById(id).orElse(null);
    }

    @Override
    public Image findByContent(byte[] content){
        return imageDbRepository.findByContent(content);
    }
}