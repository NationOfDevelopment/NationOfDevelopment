package com.sparta.nationofdevelopment.domain.review.dto.response;

import com.sparta.nationofdevelopment.domain.user.entity.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewUserResponseDto {
    private Long id;
    private String email;
    private String username;

    public static ReviewUserResponseDto of(Users user) {
        ReviewUserResponseDto responseDto = new ReviewUserResponseDto();
        responseDto.id = user.getId();
        responseDto.email = user.getEmail();
        responseDto.username = user.getUsername();
        return responseDto;
    }
}
