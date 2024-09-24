package com.sparta.nationofdevelopment.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDto {
    @Range(min = 1, max = 5)
    private int stars;

    @NotBlank
    @Size(min=1, max=200)
    private String contents;

}
