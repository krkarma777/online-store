package com.bulkpurchase.web.controller;

import com.bulkpurchase.domain.service.ImageStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UploadController {

    private final ImageStorageService imageStorageService;

    @PostMapping("/upload-image")
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleFileUpload(@RequestParam("image") MultipartFile file) {
        log.info("Received file: {}", file.getOriginalFilename());

        // 이미지를 저장하고 URL을 반환받습니다.
        String imageUrl = imageStorageService.store(file);
        log.info("Image stored at URL: {}", imageUrl);

        // 응답 메시지와 이미지 URL을 맵에 저장합니다.
        Map<String, String> response = new HashMap<>();
        response.put("message", "Image uploaded successfully");
        response.put("imageUrl", imageUrl);

        // 클라이언트에 JSON 형태로 응답을 반환합니다.
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list-images")
    @ResponseBody
    public ResponseEntity<List<String>> listUploadedFiles() {
        List<String> files = imageStorageService.listAllFiles();
        return ResponseEntity.ok(files);
    }

    @PostMapping("/delete-image")
    @ResponseBody
    public ResponseEntity<?> deleteImage(@RequestParam("filename") String filename) {
        imageStorageService.delete(filename);
        return ResponseEntity.ok("File deleted successfully");
    }
}
