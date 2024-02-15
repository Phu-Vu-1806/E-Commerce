package com.project.shopapi.service.impl;

import com.project.shopapi.entity.User;
import com.project.shopapi.model.dto.UserDto;
import com.project.shopapi.model.request.ChangePassword;
import com.project.shopapi.repository.UserRepository;
import com.project.shopapi.service.inf.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }

    @Override
    public boolean checkExistByEmail(String email) {
        User user = userRepository.findByEmail(email).get();
//        userRepository.existsByEmail(email);
        if (user != null) {
            return true;
        }

        return false;
    }

    @Override
    public List<UserDto> getAllUser(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<User> pageResult = userRepository.findAll(pageable);

        if (pageResult.hasContent()) {
            List<User> listUsers = pageResult.getContent();
            List<UserDto> dtos = convertToDto(listUsers);
            return dtos;
        } else {
            return new ArrayList<UserDto>();
        }
    }

    @Override
    public List<UserDto> getAllUserWithFilter(String keyword, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<User> pageResult = userRepository.findAll(pageable);

        if (pageResult.hasContent()) {
            List<User> listUsers = pageResult.getContent();
            List<UserDto> dtos = convertToDto(listUsers);
            return dtos;
        } else {
            return new ArrayList<UserDto>();
        }
    }

    @Override
    public void changePassword(ChangePassword req) {
        User user = userRepository.findByEmail(req.getEmail()).get();

        boolean isPasswordCorrect = false;

        if (passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            isPasswordCorrect = true;
            String encodedPassword = passwordEncoder.encode(req.getNewPassword());
            user.setPassword(encodedPassword);
            userRepository.save(user);
        }
    }

    private List<UserDto> convertToDto(List<User> listUsers) {

        List<UserDto> dtos = listUsers.stream().map(user -> UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .address(user.getAddress())
                .phone(user.getPhone())
                .build())
                .collect(Collectors.toList());
        return dtos;
    }
}
