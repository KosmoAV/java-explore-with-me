package ru.practicum.ewm.users;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.users.dto.NewUserRequest;
import ru.practicum.ewm.users.dto.UserDto;
import ru.practicum.ewm.users.interfaces.UserRepository;
import ru.practicum.ewm.users.interfaces.UserService;
import ru.practicum.ewm.users.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto addUser(NewUserRequest newUserRequest) {

        User user = UserMapper.toUser(newUserRequest);

        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {

        if (ids == null || ids.isEmpty()) {

            PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

            return userRepository.findAll(page).get()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }

        return userRepository.findAllById(ids).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {

        userRepository.deleteById(userId);
    }
}
