package com.example.bankcards.mapper;

import com.example.bankcards.dto.response.RoleResponse;
import com.example.bankcards.entity.Role;
import com.example.bankcards.exception.mapper.MappingMatchTypeException;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public <T> T toDTO(Role role, Class<T> type) {
        if (type.equals(RoleResponse.class)) {
            return type.cast(new RoleResponse(
                    role.getId(),
                    role.getName().name()
            ));
        }
        else {
            throw new MappingMatchTypeException();
        }
    }

    public Role toEntity(Object dto) {
        if (dto instanceof RoleResponse given) {
            return new Role(
                    given.id(),
                    match(given.name())
            );
        }
        else {
            throw new MappingMatchTypeException();
        }
    }

    private Role.ERole match(String name) {
        return Role.ERole.valueOf(name);
    }
}
