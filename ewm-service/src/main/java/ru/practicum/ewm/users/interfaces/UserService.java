package ru.practicum.ewm.users.interfaces;

import ru.practicum.ewm.users.dto.NewUserRequest;
import ru.practicum.ewm.users.dto.UserDto;

import java.util.List;

public interface UserService {

    public UserDto addUser(NewUserRequest newUserRequest);

    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    public void deleteUser(Long userId);
}
