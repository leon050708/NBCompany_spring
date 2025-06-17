package org.example.nbcompany.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.nbcompany.dao.NewsDao;
import org.example.nbcompany.dto.*;
import org.example.nbcompany.entity.BizNews;
import org.example.nbcompany.entity.SysUser;
import org.example.nbcompany.service.NewsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsDao newsDao;

    @Override
    @Transactional
    public NewsDto createNews(CreateNewsDto createNewsDto, SysUser currentUser) {
        boolean isSuperAdmin = currentUser.getUserType() != null && currentUser.getUserType() == 2;
        boolean isCompanyAdmin = currentUser.getCompanyRole() != null && currentUser.getCompanyRole() == 2;

        if (!isSuperAdmin && !isCompanyAdmin) {
            throw new SecurityException("您没有权限发布动态");
        }
        BizNews news = new BizNews();
        BeanUtils.copyProperties(createNewsDto, news);
        news.setAuthorId(currentUser.getId());
        news.setAuthorName(currentUser.getNickname());
        if (!isSuperAdmin) {
            news.setCompanyId(currentUser.getCompanyId());
        }
        if (isSuperAdmin) {
            news.setStatus(1);
        } else {
            news.setStatus(0);
        }
        newsDao.insert(news);
        return convertToDto(news);
    }

    @Override
    public PageInfo<NewsDto> getAllNews(int pageNum, int pageSize, NewsQueryDto queryDto, SysUser currentUser) {
        boolean isSuperAdmin = currentUser.getUserType() == 2;
        boolean isCompanyAdmin = currentUser.getCompanyRole() == 2;

        if (!isSuperAdmin) {
            queryDto.setCompanyId(null);
            Integer queryStatus = queryDto.getStatus();
            if (queryStatus == null) {
                queryDto.setStatus(1);
            } else if (queryStatus == 0 || queryStatus == 2) {
                if (isCompanyAdmin) {
                    queryDto.setCompanyId(currentUser.getCompanyId());
                } else {
                    throw new SecurityException("您无权查看此状态的动态");
                }
            }
        }

        PageHelper.startPage(pageNum, pageSize);
        List<BizNews> newsList = newsDao.findList(queryDto);
        PageInfo<BizNews> newsPageInfo = new PageInfo<>(newsList);
        List<NewsDto> newsDtoList = newsList.stream().map(this::convertToDto).collect(Collectors.toList());
        PageInfo<NewsDto> newsDtoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(newsPageInfo, newsDtoPageInfo);
        newsDtoPageInfo.setList(newsDtoList);
        return newsDtoPageInfo;
    }

    @Override
    @Transactional
    public NewsDto getNewsDetail(Long newsId, SysUser currentUser) {
        BizNews news = newsDao.findById(newsId);
        if (news == null) {
            throw new RuntimeException("动态不存在");
        }
        boolean isPublished = news.getStatus() == 1;
        if (!isPublished) {
            boolean isSuperAdmin = currentUser.getUserType() == 2;
            boolean isAuthor = Objects.equals(currentUser.getId(), news.getAuthorId());
            boolean isCompanyAdminOfAuthor = currentUser.getCompanyRole() == 2 && Objects.equals(currentUser.getCompanyId(), news.getCompanyId());
            if (!isSuperAdmin && !isAuthor && !isCompanyAdminOfAuthor) {
                throw new SecurityException("您无权查看此动态");
            }
        }
        news.setViewCount(news.getViewCount() + 1);
        newsDao.update(news);
        return convertToDto(news);
    }

    @Override
    @Transactional
    public NewsDto updateNews(Long newsId, UpdateNewsDto updateNewsDto, SysUser currentUser) {
        BizNews existingNews = newsDao.findById(newsId);
        if (existingNews == null) {
            throw new RuntimeException("动态不存在，无法更新");
        }
        boolean isSuperAdmin = currentUser.getUserType() == 2;
        boolean isCompanyAdminAndOwner = currentUser.getCompanyRole() == 2 && Objects.equals(currentUser.getCompanyId(), existingNews.getCompanyId());
        if (!isSuperAdmin && !isCompanyAdminAndOwner) {
            throw new SecurityException("您没有权限修改此动态");
        }
        BeanUtils.copyProperties(updateNewsDto, existingNews);
        newsDao.update(existingNews);
        return convertToDto(existingNews);
    }

    @Override
    @Transactional
    public void deleteNews(Long newsId, SysUser currentUser) {
        BizNews existingNews = newsDao.findById(newsId);
        if (existingNews == null) {
            return;
        }
        boolean isSuperAdmin = currentUser.getUserType() == 2;
        boolean isCompanyAdminAndOwner = currentUser.getCompanyRole() == 2 && Objects.equals(currentUser.getCompanyId(), existingNews.getCompanyId());
        if (!isSuperAdmin && !isCompanyAdminAndOwner) {
            throw new SecurityException("您没有权限删除此动态");
        }
        newsDao.deleteById(newsId);
    }

    /**
     * 新增：审核动态的实现
     */
    @Override
    @Transactional
    public void auditNews(Long newsId, AuditNewsDto auditNewsDto, SysUser currentUser) {
        // Controller层已经做了权限校验，这里是第二层保险
        if (currentUser.getUserType() != 2) {
            throw new SecurityException("只有平台超级管理员才能审核动态");
        }
        BizNews existingNews = newsDao.findById(newsId);
        if (existingNews == null) {
            throw new RuntimeException("动态不存在，无法审核");
        }
        existingNews.setStatus(auditNewsDto.getStatus());
        newsDao.update(existingNews);
    }

    private NewsDto convertToDto(BizNews news) {
        if (news == null) {
            return null;
        }
        NewsDto newsDto = new NewsDto();
        BeanUtils.copyProperties(news, newsDto);
        return newsDto;
    }
}