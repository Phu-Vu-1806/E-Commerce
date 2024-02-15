package com.project.shopapi.service.inf;

import com.project.shopapi.entity.User;
import com.project.shopapi.model.dto.UserDto;
import com.project.shopapi.model.request.ChangePassword;

import java.util.List;

public interface UserService{
    public void save(User user);
    public User findByEmail(String email);
    public User findByUsername(String username);
    public boolean checkExistByEmail(String email);
    public List<UserDto> getAllUser(Integer pageNo, Integer pageSize, String sortBy);
    public List<UserDto> getAllUserWithFilter(String keyword, Integer pageNo, Integer pageSize, String sortBy);
    public void changePassword(ChangePassword req);
}
