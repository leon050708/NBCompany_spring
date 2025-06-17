package org.example.nbcompany.controller;

import com.github.pagehelper.PageInfo;
import org.example.nbcompany.dto.CreateNewsDto;
import org.example.nbcompany.dto.NewsDto;
import org.example.nbcompany.dto.NewsQueryDto; // 引入 NewsQueryDto
import org.example.nbcompany.dto.UpdateNewsDto;
import org.example.nbcompany.security.CustomUserDetails;
import org.example.nbcompany.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @PostMapping
    public ResponseEntity<?> createNews(@RequestBody CreateNewsDto createNewsDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            NewsDto createdNews = newsService.createNews(createNewsDto, userDetails.getSysUser());
            return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("创建失败: " + e.getMessage());
        }
    }

    /**
     * 修正后的获取动态列表的API端点
     */
    @GetMapping
    public ResponseEntity<PageInfo<NewsDto>> getAllNews(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            // Spring MVC 会自动将请求中的同名字段封装到 queryDto 对象中
            NewsQueryDto queryDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 调用 Service 层中最新的、包含4个参数的方法
        PageInfo<NewsDto> newsPage = newsService.getAllNews(pageNum, pageSize, queryDto, userDetails.getSysUser());
        return ResponseEntity.ok(newsPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNewsDetail(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            if (userDetails == null) { throw new SecurityException("用户未登录"); }
            NewsDto newsDto = newsService.getNewsDetail(id, userDetails.getSysUser());
            return ResponseEntity.ok(newsDto);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNews(@PathVariable Long id,
                                        @RequestBody UpdateNewsDto updateNewsDto,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            NewsDto updatedNews = newsService.updateNews(id, updateNewsDto, userDetails.getSysUser());
            return ResponseEntity.ok(updatedNews);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Long id,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            newsService.deleteNews(id, userDetails.getSysUser());
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}