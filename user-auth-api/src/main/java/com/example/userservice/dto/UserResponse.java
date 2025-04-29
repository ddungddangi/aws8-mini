package com.example.userservice.dto;

import com.example.userservice.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private String email;
    private String nickname;

    public UserResponse(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }
}
