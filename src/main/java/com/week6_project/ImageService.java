package com.week6_project;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final AmazonS3 s3Client;
    private final String bucketName = "agbame2";


public String uploadImage(MultipartFile file) throws IOException {
    String fileName = file.getOriginalFilename();
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(file.getContentType());
    s3Client.putObject(bucketName, fileName, file.getInputStream(), objectMetadata);

    return s3Client.getUrl(bucketName, fileName).toString();
}


    public void deleteImage(Integer id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // Extract filename from the full URL
        String fileName = extractFileNameFromUrl(image.getUrl());

        // Delete from S3
        s3Client.deleteObject(bucketName, fileName);

        // Delete from database
        imageRepository.delete(image);
    }

    private String extractFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }


}
