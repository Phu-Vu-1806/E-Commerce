package com.project.shopapi.controller;

import com.project.shopapi.annotation.ValidImage;
import com.project.shopapi.entity.File;
import com.project.shopapi.entity.User;
import com.project.shopapi.model.request.UploadFile;
import com.project.shopapi.payload.request.UploadImg;
import com.project.shopapi.security.service.UserDetailsImpl;
import com.project.shopapi.service.impl.FileServiceImpl;
import com.project.shopapi.service.impl.UserServiceImpl;
import com.project.shopapi.utils.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/file")
@AllArgsConstructor
public class FileController {

    private final FileServiceImpl fileService;
    private final UserServiceImpl userService;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadFile(@RequestPart("file") @ValidImage MultipartFile file,
                                                      Authentication authentication) {
        String message = null;
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
//        MultipartFile file = uploadImg.getImage();
        try {
            fileService.save(file, user);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UploadFile>> getListFiles() {
        List<UploadFile> files = fileService.getAllFile().map(file -> {
//            tạo đường dẫn cho file ảnh
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/file/")
                    .path(file.getId())
                    .toUriString();

            return new UploadFile(
                    file.getName(),
                    fileDownloadUri,
                    file.getType(),
                    file.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        File file = fileService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getData());
    }
}
