package org.example.nbcompany.dto;

import lombok.Data;


@Data
public class UpdateNewsDto {
    private String title;
    private String coverImageUrl;
    private String summary;
    private String content;
}