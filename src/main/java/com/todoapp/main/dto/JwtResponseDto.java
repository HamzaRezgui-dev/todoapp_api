package com.todoapp.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDto {
    private String accessToken;
    private String refreshToken;

    public static JwtResponseDto of(String accessToken, String refreshToken) {
        return new JwtResponseDto(accessToken, refreshToken);
    }
}
