package org.example.nbcompany.controller;

import org.example.nbcompany.dto.AuditNewsDto;
import org.example.nbcompany.security.CustomUserDetails;
import org.example.nbcompany.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/news")
// 使用 @PreAuthorize 注解，整个Controller都需要超级管理员权限
@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
public class AdminNewsController {

    @Autowired
    private NewsService newsService;

    @PutMapping("/{id}/status")
    public ResponseEntity<?> auditNews(
            @PathVariable Long id,
            @RequestBody AuditNewsDto auditDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        newsService.auditNews(id, auditDto, userDetails.getSysUser());
        return ResponseEntity.ok().build();
    }
}