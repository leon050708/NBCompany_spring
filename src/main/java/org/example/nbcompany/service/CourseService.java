package org.example.nbcompany.service;

import com.github.pagehelper.PageInfo;
import org.example.nbcompany.dao.BizCourseDao;
import org.example.nbcompany.dto.CourseDTO;



public interface CourseService {
    CourseDTO createCourse(CourseDTO CourseDTO);

    CourseDTO getCourseById(Long id);

    PageInfo<CourseDTO> getAllUsers(int pageNum, int pageSize);

    CourseDTO updateUser(Long id, CourseDTO courseDTO);

    void deleteUser(Long id);
}
