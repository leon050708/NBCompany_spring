package org.example.nbcompany.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.nbcompany.entity.BizNews;
import java.util.List;

@Mapper
public interface NewsDao {
    int insert(BizNews news);
    BizNews findById(Long id);
    // PageHelper 会自动对这个查询进行分页
    List<BizNews> findAll();
    int update(BizNews news);
    int deleteById(Long id);
}