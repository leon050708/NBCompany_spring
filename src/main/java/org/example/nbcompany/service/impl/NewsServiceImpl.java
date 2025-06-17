package org.example.nbcompany.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.nbcompany.dao.NewsDao;
import org.example.nbcompany.dto.CreateNewsDto;
import org.example.nbcompany.dto.NewsDto;
import org.example.nbcompany.dto.UpdateNewsDto; // 稍后会用到
import org.example.nbcompany.dto.AuditNewsDto; // 稍后会用到
import org.example.nbcompany.entity.BizNews;
import org.example.nbcompany.entity.SysUser;
import org.example.nbcompany.service.NewsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.nbcompany.dto.UpdateNewsDto;
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
        // 1. 权限检查：只有平台超级管理员(userType=2)或企业管理员(companyRole=2)可以发布
        boolean isSuperAdmin = currentUser.getUserType() != null && currentUser.getUserType() == 2;
        boolean isCompanyAdmin = currentUser.getCompanyRole() != null && currentUser.getCompanyRole() == 2;

        if (!isSuperAdmin && !isCompanyAdmin) {
            throw new SecurityException("您没有权限发布动态");
        }

        // 2. 将 DTO 转换为 BizNews 实体
        BizNews news = new BizNews();
        BeanUtils.copyProperties(createNewsDto, news);

        // 3. 填充作者和公司信息
        news.setAuthorId(currentUser.getId());
        news.setAuthorName(currentUser.getNickname());
        // 平台超管没有公司ID，可以为null
        if (!isSuperAdmin) {
            news.setCompanyId(currentUser.getCompanyId());
        }

        // 4. 设置状态：平台超级管理员直接发布，企业管理员发布的需要审核
        if (isSuperAdmin) {
            news.setStatus(1); // 1: 已发布
        } else {
            news.setStatus(0); // 0: 待审核
        }

        // 5. 调用 DAO 将数据存入数据库
        newsDao.insert(news);

        // 6. 将保存后的完整实体（现在包含ID）转换回 DTO 并返回
        return convertToDto(news);
    }

    @Override
    public PageInfo<NewsDto> getAllNews(int pageNum, int pageSize, SysUser currentUser) {
        // TODO: 实现列表查询逻辑
        PageHelper.startPage(pageNum, pageSize);
        List<BizNews> newsList = newsDao.findAll(); // 这里的查询逻辑需要根据权限细化
        PageInfo<BizNews> newsPageInfo = new PageInfo<>(newsList);

        List<NewsDto> newsDtoList = newsList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        PageInfo<NewsDto> newsDtoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(newsPageInfo, newsDtoPageInfo);
        newsDtoPageInfo.setList(newsDtoList);

        return newsDtoPageInfo;
    }

    // 我们还需要补充 update, delete, getById, audit 等方法的实现


    @Transactional // 因为有更新操作（viewCount+1），所以需要事务
    public NewsDto getNewsDetail(Long newsId, SysUser currentUser) {
        // 1. 从数据库查找动态
        BizNews news = newsDao.findById(newsId);
        if (news == null) {
            throw new RuntimeException("动态不存在"); // 实际项目中建议自定义 NotFoundException
        }

        // 2. 权限判断：
        // 规则：已发布的(status=1)谁都能看。未发布的(status=0或2)只有超管、作者本人、作者所属公司的企业管理员能看。
        boolean isPublished = news.getStatus() == 1;
        if (!isPublished) {
            boolean isSuperAdmin = currentUser.getUserType() == 2;
            boolean isAuthor = Objects.equals(currentUser.getId(), news.getAuthorId());
            boolean isCompanyAdminOfAuthor = currentUser.getCompanyRole() == 2 && Objects.equals(currentUser.getCompanyId(), news.getCompanyId());

            if (!isSuperAdmin && !isAuthor && !isCompanyAdminOfAuthor) {
                throw new SecurityException("您无权查看此动态");
            }
        }

        // 3. 浏览次数 +1
        news.setViewCount(news.getViewCount() + 1);
        newsDao.update(news); // 将增加后的浏览次数更新回数据库

        // 4. 转换为 DTO 并返回
        return convertToDto(news);
    }

    private NewsDto convertToDto(BizNews news) {
        if (news == null) {
            return null;
        }
        NewsDto newsDto = new NewsDto();
        BeanUtils.copyProperties(news, newsDto);
        return newsDto;
    }
    @Override
    @Transactional
    public NewsDto updateNews(Long newsId, UpdateNewsDto updateNewsDto, SysUser currentUser) {
        // 1. 从数据库查找现有的动态
        BizNews existingNews = newsDao.findById(newsId);
        if (existingNews == null) {
            throw new RuntimeException("动态不存在，无法更新");
        }

        // 2. 权限判断：只有平台超级管理员 或 这篇动态所属公司的企业管理员 才能修改
        boolean isSuperAdmin = currentUser.getUserType() == 2;
        boolean isCompanyAdminAndOwner = currentUser.getCompanyRole() == 2 &&
                java.util.Objects.equals(currentUser.getCompanyId(), existingNews.getCompanyId());

        if (!isSuperAdmin && !isCompanyAdminAndOwner) {
            throw new SecurityException("您没有权限修改此动态");
        }

        // 3. 将 DTO 中的数据更新到查询出的实体中
        BeanUtils.copyProperties(updateNewsDto, existingNews);

        // 4. 调用 DAO 更新数据库
        newsDao.update(existingNews);

        // 5. 返回更新后的数据
        return convertToDto(existingNews);
    }

    @Override
    @Transactional
    public void deleteNews(Long newsId, SysUser currentUser) {
        // 1. 从数据库查找动态，主要用于后续的权限判断
        BizNews existingNews = newsDao.findById(newsId);
        if (existingNews == null) {
            // 如果动态本身就不存在，我们可以认为删除操作已经“完成”，直接返回即可，无需报错。
            return;
        }

        // 2. 权限判断 (逻辑与更新功能完全相同)
        // 只有平台超级管理员 或 这篇动态所属公司的企业管理员 才能删除
        boolean isSuperAdmin = currentUser.getUserType() == 2;
        boolean isCompanyAdminAndOwner = currentUser.getCompanyRole() == 2 &&
                java.util.Objects.equals(currentUser.getCompanyId(), existingNews.getCompanyId());

        if (!isSuperAdmin && !isCompanyAdminAndOwner) {
            throw new SecurityException("您没有权限删除此动态");
        }

        // 3. 调用 DAO 执行删除操作
        newsDao.deleteById(newsId);
    }
}