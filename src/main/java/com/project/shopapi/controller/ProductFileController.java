package com.project.shopapi.controller;

import com.project.shopapi.annotation.ValidImage;
import com.project.shopapi.entity.ProductFile;
import com.project.shopapi.entity.User;
import com.project.shopapi.exception.ImageNotFoundException;
import com.project.shopapi.model.request.UploadFile;
import com.project.shopapi.security.service.UserDetailsImpl;
import com.project.shopapi.service.impl.ProductFileServiceImpl;
import com.project.shopapi.service.impl.UserServiceImpl;
import com.project.shopapi.utils.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product-file")
public class ProductFileController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ProductFileServiceImpl productFileService;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/upload/{productId}")
    public ResponseEntity<MessageResponse> uploadFile(@RequestParam("file") @ValidImage MultipartFile file,
                                                      @PathVariable(name = "productId") int productId,
                                                      Authentication authentication) {
        String message = null;
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user =  userService.findByUsername(userDetails.getUsername());

        if (!file.getOriginalFilename().equals(null)) {
            System.out.println(file.getOriginalFilename());		//tên của file upload lên
            java.io.File file2 = new java.io.File("C:\\ProductImg");
            if(!file2.exists()) {	//kiểm tra xem file2 đã tồn tại chưa, nếu chưa tồn tại sẽ tạo ra 1 folder mới
                file2.mkdir();
            }
            try {
                java.io.File newFile = new java.io.File("C:\\ProductImg\\" + file.getOriginalFilename());

                FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                fileOutputStream.write(file.getBytes());	//lưu file
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {

            productFileService.save(file, user, productId);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UploadFile>> getListFiles() {
        List<UploadFile> files = productFileService.getAllFile().stream().map(file -> {
//            tạo đường dẫn cho file ảnh
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/product-file/get/")
                    .path(file.getName())
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
        ProductFile productFile = productFileService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + productFile.getName() + "\"")
                .body(productFile.getData());
    }

    private static final String FILE_PATH_ROOT = "C:\\ProductImg\\";

    @GetMapping("/get/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable("filename") String filename) {
        byte[] image = new byte[0];
        System.out.println(FILE_PATH_ROOT);
//      File file = new File("C:\\Users\\Vu Trong Phu\\Downloads\\java.png");
        try {
            image = org.apache.commons.io.FileUtils.readFileToByteArray(new java.io.File(FILE_PATH_ROOT+filename));
//          image = org.apache.commons.io.FileUtils.readFileToByteArray(file);
            System.out.println("image "+image);
        } catch (IOException e) {
//          e.printStackTrace();
            throw new ImageNotFoundException("Image file not found");
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
}
