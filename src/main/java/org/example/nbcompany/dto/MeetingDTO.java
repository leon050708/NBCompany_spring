package org.example.nbcompany.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MeetingDTO {
    private Long id;
    private String meetingName;

    // @JsonFormat 注解可以保证返回给前端的日期时间格式是我们想要的
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private String coverImageUrl;
    private String content;
    private String location;
    private String organizer;
    private String agenda;
    private String speakers;
    private Long creatorId;
    private String creatorName;
    private Long companyId;
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

}
