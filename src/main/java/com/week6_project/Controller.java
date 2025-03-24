package com.week6_project;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class Controller {
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    @GetMapping
    public Page<Image> getImages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return imageRepository.findAllByOrderByIdDesc(PageRequest.of(page, size));
    }

    @PostMapping
    public Image uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description) throws IOException {
        String url = imageService.uploadImage(file);

        Image image = new Image();
        image.setCreated(LocalDateTime.now());
        image.setDescription(description);
        image.setUrl(url);

        return imageRepository.save(image);
    }
}
