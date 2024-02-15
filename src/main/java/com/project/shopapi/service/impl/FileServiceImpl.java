package com.project.shopapi.service.impl;

import com.project.shopapi.entity.File;
import com.project.shopapi.entity.User;
import com.project.shopapi.repository.FileRepository;
import com.project.shopapi.service.inf.FileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final UserServiceImpl userService;

    @Override
    public File save(MultipartFile file, User user) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        File file1 = new File(fileName, file.getContentType(), file.getBytes(), userService.findByUsername(user.getUsername()));
        return fileRepository.save(file1);
    }

    @Override
    public File getFile(String id) {
        return fileRepository.findById(id).get();
    }

    @Override
    public Stream<File> getAllFile() {
        return fileRepository.findAll().stream();
    }
}
