package org.example.nbcompany.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.nbcompany.entity.BizMeeting;
import java.util.List;

@Mapper
public interface BizMeetingDao {
    // 插入一条新的会议记录
    int insert(BizMeeting meeting);

    // 根据ID查找一个会议
    BizMeeting findById(@Param("id") Long id);

    // 查找所有会议（PageHelper分页插件会自动拦截这个方法来实现分页）
    List<BizMeeting> findAll();

    // 更新一个已存在的会议
    int update(BizMeeting meeting);

    // 根据ID删除一个会议
    int deleteById(@Param("id") Long id);
}
