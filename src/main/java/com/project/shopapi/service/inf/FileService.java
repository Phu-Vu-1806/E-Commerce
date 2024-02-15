package com.project.shopapi.service.inf;

import com.project.shopapi.entity.File;
import com.project.shopapi.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

public interface FileService {
    public File save(MultipartFile file, User user) throws IOException;
    public File getFile(String id);
    public Stream<File> getAllFile();
}
