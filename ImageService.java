package com.example.ImagePostingApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }
    
    
    // Extra
    public Image getImageById(Long id) {
        return imageRepository.findById(id).get();
    }

    public void incrementLikeCount(Long id) {
        Image image = imageRepository.findById(id).orElseThrow();
        image.setLikeCount(image.getLikeCount() + 1);
        imageRepository.save(image);
    }
}
