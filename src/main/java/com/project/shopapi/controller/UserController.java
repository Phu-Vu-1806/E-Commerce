package com.project.shopapi.controller;

import com.project.shopapi.entity.User;
import com.project.shopapi.model.dto.UserDto;
import com.project.shopapi.model.request.ChangePassword;
import com.project.shopapi.security.service.UserDetailsImpl;
import com.project.shopapi.service.impl.UserServiceImpl;
import com.project.shopapi.utils.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUser(@RequestParam(defaultValue = "0") int pageNo,   //pageNo là trang thứ bao nhiêu?(pageNo sẽ được bắt đầu từ 0 vì vậy nếu muốn hiển thị trang đầu phải là 0)
                                                    @RequestParam(defaultValue = "10") int pageSize,//pageSize là trang chứa bao nhiêu kết quả(mặc định pageSize=10 kết quả hiển thị)
                                                    @RequestParam(defaultValue = "id") String sortBy){  //sortBy là trang xếp thứ tự theo gì?( mặc định theo id)
        List<UserDto> dtos = userService.getAllUser(pageNo, pageSize, sortBy);
        return new ResponseEntity<List<UserDto>>(dtos, HttpStatus.OK);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUserWithFilter(@RequestParam(defaultValue = "0") int pageNo,
                                                              @RequestParam(defaultValue = "10") int pageSize,
                                                              @RequestParam(defaultValue = "id") String sortBy,
                                                              @RequestParam(defaultValue = "keyword") String keyword){
        List<UserDto> dtos = userService.getAllUserWithFilter(keyword, pageNo, pageSize, sortBy);

        return new ResponseEntity<List<UserDto>>(dtos, HttpStatus.OK);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePassword req) {

        userService.changePassword(req);
        return new ResponseEntity<MessageResponse>(new MessageResponse("Sucesss"), HttpStatus.OK);

    }

    @GetMapping("/me")
    public ResponseEntity<?> getUser(HttpServletRequest request, Authentication authentication){

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user =  userService.findByUsername(userDetails.getUsername());
        UserDto userDto = new UserDto(user.getId(), user.getEmail(), user.getUsername(), user.getAddress(), user.getPhone());
        return ResponseEntity.ok(userDto);
    }

}
