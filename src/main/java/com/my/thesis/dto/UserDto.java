package com.my.thesis.dto;

import com.my.thesis.model.User;
import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;

    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setEmail(email);

        return user;
    }

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstname());
        userDto.setLastName(user.getLastname());
        userDto.setEmail(user.getEmail());

        return userDto;
    }
}