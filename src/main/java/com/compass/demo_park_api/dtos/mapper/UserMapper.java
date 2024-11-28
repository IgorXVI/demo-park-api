package com.compass.demo_park_api.dtos.mapper;

import com.compass.demo_park_api.dtos.UserCreateDTO;
import com.compass.demo_park_api.dtos.UserResponseDTO;
import com.compass.demo_park_api.entities.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

public class UserMapper {
    public static User toUser(UserCreateDTO createDTO) {
        return new ModelMapper().map(createDTO, User.class);
    }

    public static UserResponseDTO toResponseDTO(User user) {
        String role = user.getRole().name().substring("ROLE_".length());
        PropertyMap<User, UserResponseDTO> props = new PropertyMap<User, UserResponseDTO>() {
            protected void configure() {
                map().setRole(role);
            }
        };
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(props);
        return modelMapper.map(user, UserResponseDTO.class);
    }
}
