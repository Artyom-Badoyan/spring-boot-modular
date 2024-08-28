package ru.itmentor.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itmentor.common.entity.User;
import ru.itmentor.rest.dto.CreateUserRequestDto;
import ru.itmentor.rest.dto.CreateUserResponseDto;
import ru.itmentor.rest.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User map(CreateUserRequestDto dto);

    CreateUserResponseDto mapToResponse(User user);

    UserDto mapToDto(User user);

}
