package org.example.nbcompany.controller;

import com.github.pagehelper.PageInfo;
import org.example.nbcompany.dto.CreateNewsDto;
import org.example.nbcompany.dto.NewsDto;
import org.example.nbcompany.security.CustomUserDetails;
import org.example.nbcompany.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.example.nbcompany.dto.UpdateNewsDto;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @PostMapping
    public ResponseEntity<?> createNews(
            @RequestBody CreateNewsDto createNewsDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        try {
            NewsDto createdNews = newsService.createNews(createNewsDto, userDetails.getSysUser());
            return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("创建失败: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<PageInfo<NewsDto>> getAllNews(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        PageInfo<NewsDto> newsPage = newsService.getAllNews(pageNum, pageSize, userDetails.getSysUser());
        return ResponseEntity.ok(newsPage);
    }

    /**
     * 获取动态详情的API端点
     * 修正了 catch 块的顺序
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getNewsDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            // Service层可能为null，需要处理
            if (userDetails == null) {
                // 对于允许匿名访问的接口，userDetails可能为null
                // 此处我们假设详情页需要登录，如果不需要，则传入null
                throw new SecurityException("用户未登录");
            }
            NewsDto newsDto = newsService.getNewsDetail(id, userDetails.getSysUser());
            return ResponseEntity.ok(newsDto);
        } catch (SecurityException e) { // 范围小的 SecurityException 在前
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) { // 范围大的 RuntimeException 在后
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
    /**
     * 删除动态的API端点
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Long id,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            newsService.deleteNews(id, userDetails.getSysUser());
            // 对于成功的DELETE操作，HTTP规范推荐返回 204 No Content
            // 这表示服务器成功处理了请求，但没有内容返回。
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}