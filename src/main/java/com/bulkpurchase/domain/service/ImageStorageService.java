package com.bulkpurchase.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class ImageStorageService {

    // 이미지를 저장할 디렉토리 경로
    private final Path rootLocation = Paths.get("uploaded-images");

    public ImageStorageService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public void delete(String filename) {
        try {
            // 파일 경로 생성
            Path file = rootLocation.resolve(filename);

            // 파일 존재 여부 확인 후 삭제
            if (Files.exists(file)) {
                Files.delete(file);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    public String store(MultipartFile file) {
        try {
            // 파일 이름 생성 (중복을 피하기 위해 현재 시간을 파일 이름에 포함)
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path destinationFile = rootLocation.resolve(Paths.get(filename))
                    .normalize().toAbsolutePath();
            // 파일 저장
            file.transferTo(destinationFile);

            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}
