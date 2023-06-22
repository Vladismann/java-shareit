package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.common.CreateGroup;
import ru.practicum.shareit.common.UpdateGroup;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static ru.practicum.shareit.user.UserMessages.EMPTY_EMAIL;
import static ru.practicum.shareit.user.UserMessages.INCORRECT_EMAIL;

@AllArgsConstructor
@Data
public class UserDto {
    private long id;
    private String name;
    @NotBlank(message = EMPTY_EMAIL, groups = CreateGroup.class)
    @Email(message = INCORRECT_EMAIL, groups = {CreateGroup.class, UpdateGroup.class})
    private String email;
}
