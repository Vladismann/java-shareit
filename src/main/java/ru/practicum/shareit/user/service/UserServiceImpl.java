package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepo;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.dto.UserMapper.fromUserDto;
import static ru.practicum.shareit.user.dto.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = fromUserDto(userDto);
        return toUserDto(userRepo.create(user));
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        userDto.setId(id);
        User user = fromUserDto(userDto);
        return toUserDto(userRepo.update(user));
    }

    @Override
    public void deleteUser(long id) {
        userRepo.delete(id);
    }

    @Override
    public UserDto getUser(long id) {
        return toUserDto(userRepo.get(id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepo.getAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
