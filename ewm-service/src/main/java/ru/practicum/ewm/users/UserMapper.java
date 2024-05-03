package ru.practicum.ewm.users;

import ru.practicum.ewm.users.dto.NewUserRequest;
import ru.practicum.ewm.users.dto.UserDto;
import ru.practicum.ewm.users.model.User;

public class UserMapper {

    public static User toUser(NewUserRequest newUserRequest) {

        User user = new User();
        user.setName(newUserRequest.getName());
        user.setEmail(newUserRequest.getEmail());

        return user;
    }

    public static UserDto toUserDto(User user) {

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }
}
