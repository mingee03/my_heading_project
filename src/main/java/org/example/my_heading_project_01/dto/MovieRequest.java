package org.example.my_heading_project_01.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MovieRequest(
        @NotBlank(message = "제목은 필수입니다.")
        String title,

        @NotBlank(message = "감독은 필수입니다.")
        String director,

        @NotBlank(message = "장르는 필수입니다.")
        String genre,

        @NotBlank(message = "개봉 연도는 필수입니다.")
        String year,

        @Min(value = 0, message = "관객수는 0 이상이어야 합니다.")
        int view
) {}
