package org.example.nbcompany.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 课程表 (biz_course) 对应的实体类
 */
@Data
public class BizCourse implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 课程ID */
    private Long id;

    /** 课程名称 */
    private String courseName;

    /** 课程封面URL */
    private String coverImageUrl;

    /** 课程简介 */
    private String summary;

    /** 课程视频URL */
    private String courseVideoUrl;

    /** 课程排序值 */
    private Integer sortOrder;

    /** 作者ID (发布者) */
    private Long authorId;

    /** 作者名称 */
    private String authorName;

    /** 发布企业ID */
    private Long companyId;

    /** 审核状态 (0:待审核, 1:已发布/审核通过, 2:审核未通过) */
    private Integer status;

    /** 观看次数 */
    private Integer viewCount;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    public BizCourse(Long id, String courseName, String coverImageUrl, String summary, String courseVideoUrl, Integer sortOrder, Long authorId, String authorName, Long companyId, Integer status, Integer viewCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.courseName = courseName;
        this.coverImageUrl = coverImageUrl;
        this.summary = summary;
        this.courseVideoUrl = courseVideoUrl;
        this.sortOrder = sortOrder;
        this.authorId = authorId;
        this.authorName = authorName;
        this.companyId = companyId;
        this.status = status;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public BizCourse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCourseVideoUrl() {
        return courseVideoUrl;
    }

    public void setCourseVideoUrl(String courseVideoUrl) {
        this.courseVideoUrl = courseVideoUrl;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
