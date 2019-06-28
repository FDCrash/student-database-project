package com.netcracker.denisik.converters;

import com.netcracker.denisik.dto.RoleDTO;
import com.netcracker.denisik.dto.UserDTO;
import com.netcracker.denisik.entities.Role;
import com.netcracker.denisik.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public User convert(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .role(Role.valueOf(userDTO.getRoleDTO().name()))
                .login(userDTO.getLogin())
                .password(userDTO.getPassword())
                .name(userDTO.getName())
                .build();
    }

    public UserDTO convert(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .roleDTO(RoleDTO.valueOf(user.getRole().name()))
                .login(user.getLogin())
                .password(user.getPassword())
                .name(user.getName())
                .build();
    }
}
