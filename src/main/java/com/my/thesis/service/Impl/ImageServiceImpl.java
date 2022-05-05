package com.my.thesis.service.Impl;

import com.my.thesis.model.Image;
import com.my.thesis.repository.ImageDbRepository;
import com.my.thesis.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

        return imageDbRepository.save(dbImage);
    }

    @Override
    public Resource downloadImage(Image image) {

        byte[] result = image.getContent();

        return new ByteArrayResource(result);
    }
}