package com.example.bankcards.service;

import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.entity.UserNotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

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

    public Set<UserResponse> findAll() {
        var users = userRepository.findAll();
        return users.stream()
                .map(x -> userMapper.toDTO(x, UserResponse.class))
                .collect(Collectors.toSet());
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public User getCurrentAuthenticatedUser() {
        var authentication = (User) SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getUsername())
                .orElseThrow(UserNotFoundException::new);
    }
}