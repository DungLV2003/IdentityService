package com.devteria.identityservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.devteria.identityservice.dto.request.UserCreationRequest;
import com.devteria.identityservice.dto.request.UserUpdateRequest;
import com.devteria.identityservice.dto.response.UserResponse;
import com.devteria.identityservice.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    // @Mapping(source = "firstName", target = "lastName") //mapp firstName sang lastName
    // @Mapping(target = "lastName", ignore = true) //khong mapping field lastName
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true) // k map role o cho nay
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
