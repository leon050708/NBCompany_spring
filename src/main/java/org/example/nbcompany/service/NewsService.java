package org.example.nbcompany.service;

import com.github.pagehelper.PageInfo;
import org.example.nbcompany.dto.CreateNewsDto;
import org.example.nbcompany.dto.NewsDto;
import org.example.nbcompany.dto.UpdateNewsDto; // 引入
import org.example.nbcompany.entity.SysUser;

public interface NewsService {

    NewsDto createNews(CreateNewsDto createNewsDto, SysUser currentUser);

    PageInfo<NewsDto> getAllNews(int pageNum, int pageSize, SysUser currentUser);

    NewsDto getNewsDetail(Long newsId, SysUser currentUser);

    // 新增更新方法
    NewsDto updateNews(Long newsId, UpdateNewsDto updateNewsDto, SysUser currentUser);

    // 马上要实现删除方法
    void deleteNews(Long newsId, SysUser currentUser);
}