package com.example.bankcards.service;

import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.exception.service.UserNotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserResponse findByUsername(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        return userMapper.toDTO(user, UserResponse.class);
    }
}