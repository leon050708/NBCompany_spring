package org.example.nbcompany.service;

import com.github.pagehelper.PageInfo;
import org.example.nbcompany.dto.AuditNewsDto;
import org.example.nbcompany.dto.CreateNewsDto;
import org.example.nbcompany.dto.NewsDto;
import org.example.nbcompany.dto.NewsQueryDto;
import org.example.nbcompany.dto.UpdateNewsDto;
import org.example.nbcompany.entity.SysUser; // 引入SysUser

/**
 * 行业动态业务逻辑接口 (最终修正版)
 */
public interface NewsService {

    /**
     * 创建新的行业动态
     * @param createNewsDto 包含动态信息的DTO
     * @param currentUser   当前操作的用户实体
     * @return 返回创建成功后的动态DTO
     */
    NewsDto createNews(CreateNewsDto createNewsDto, SysUser currentUser);

    /**
     * 获取动态列表
     * @param queryDto 查询参数
     * @param currentUser 当前用户
     * @return 分页后的动态列表
     */
    PageInfo<NewsDto> getAllNews(int pageNum, int pageSize, NewsQueryDto queryDto, SysUser currentUser);

    /**
     * 获取单个动态详情
     * @param newsId 动态ID
     * @param currentUser 当前用户
     * @return 动态详情DTO
     */
    NewsDto getNewsDetail(Long newsId, SysUser currentUser);

    /**
     * 更新动态
     */
    NewsDto updateNews(Long newsId, UpdateNewsDto updateNewsDto, SysUser currentUser);

    /**
     * 删除动态
     */
    void deleteNews(Long newsId, SysUser currentUser);

    /**
     * 审核动态
     */
    void auditNews(Long newsId, AuditNewsDto auditNewsDto, SysUser currentUser);
}