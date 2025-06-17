package org.example.nbcompany.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NewsDto {
    private Long id;
    private String title;
    private String coverImageUrl;
    private String summary;
    private String content; // 详情视图可能包含内容
    private Long authorId;
    private String authorName;
    private Long companyId;
    private Integer status;
    private Integer viewCount;
    private LocalDateTime createdAt;
}