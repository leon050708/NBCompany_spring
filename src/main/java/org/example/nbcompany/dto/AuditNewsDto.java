package org.example.nbcompany.dto;

import lombok.Data;

@Data
public class AuditNewsDto {
    private Integer status; // 1: 审核通过, 2: 审核未通过
}