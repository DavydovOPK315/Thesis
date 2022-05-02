package com.my.thesis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.my.thesis.model.Status;
import com.my.thesis.model.User;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminUserDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String status;

    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setEmail(email);
        user.setStatus(Status.valueOf(status));
        return user;
    }

    public static AdminUserDto fromUser(User user) {
        AdminUserDto AdminUserDto = new AdminUserDto();
        AdminUserDto.setId(user.getId());
        AdminUserDto.setUsername(user.getUsername());
        AdminUserDto.setFirstName(user.getFirstname());
        AdminUserDto.setLastName(user.getLastname());
        AdminUserDto.setEmail(user.getEmail());
        AdminUserDto.setStatus(user.getStatus().name());
        return AdminUserDto;
    }
}
