package com.example.bankcards.mapper;

import com.example.bankcards.dto.response.RoleResponse;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.mapper.MappingMatchTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final RoleMapper roleMapper;

    public <T> T toDTO(User user, Class<T> type) {
        var setOfRoleResponse = user.getRoles().stream()
                .map(x -> roleMapper.toDTO(x, RoleResponse.class))
                .collect(Collectors.toSet());
        if (type.equals(UserResponse.class)) {
            return type.cast(new UserResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    setOfRoleResponse,
                    null
            ));
        }
        else {
            throw new MappingMatchTypeException();
        }
    }

    public User toEntity(Object dto) {
        if (dto instanceof UserResponse given) {
            var setOfRole = given.roles().stream()
                    .map(roleMapper::toEntity)
                    .collect(Collectors.toSet());
            return new User(
                    given.id(),
                    given.username(),
                    given.password(),
                    setOfRole,
                    null,
                    null
            );
        }
        else {
            throw new  MappingMatchTypeException();
        }

    }
}
