package com.example.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserUpdateRequest {

    @NotBlank(message = "닉네임은 비어 있을 수 없습니다.")
    private String nickname;
}
