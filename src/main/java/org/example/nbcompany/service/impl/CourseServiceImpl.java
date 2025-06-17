package org.example.nbcompany.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.nbcompany.dao.BizCourseDao;
import org.example.nbcompany.dto.CourseDTO;
import org.example.nbcompany.entity.BizCourse;
import org.example.nbcompany.service.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private BizCourseDao bizCourseDao;

    @Override
    @Transactional
    public CourseDTO createCourse(CourseDTO courseDTO) {
        BizCourse bizCourse = new BizCourse();
        BeanUtils.copyProperties(courseDTO, bizCourse);
        bizCourseDao.insert(bizCourse);
        courseDTO.setId(bizCourse.getId()); // 回填主键
        return courseDTO;
    }

    @Override
    public CourseDTO getCourseById(Long id) {
        BizCourse bizCourse = bizCourseDao.findById(id);
        if (bizCourse == null) return null;
        return convertToDto(bizCourse);
    }

    @Override
    public PageInfo<CourseDTO> getAllUsers(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<BizCourse> courseList = bizCourseDao.findAll();
        List<CourseDTO> dtoList = courseList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new PageInfo<>(dtoList);
    }

    @Override
    @Transactional
    public CourseDTO updateUser(Long id, CourseDTO courseDTO) {
        BizCourse existing = bizCourseDao.findById(id);
        if (existing == null) return null;

        BeanUtils.copyProperties(courseDTO, existing);
        existing.setId(id);
        bizCourseDao.update(existing);

        return convertToDto(existing);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        bizCourseDao.deleteById(id);
    }

    private CourseDTO convertToDto(BizCourse bizCourse) {
        CourseDTO courseDTO = new CourseDTO();
        BeanUtils.copyProperties(bizCourse, courseDTO);
        return courseDTO;
    }
}