package com.lihe.dao;

import com.lihe.pojo.Student;

import java.util.List;

public interface StudentMapper {
    // 查询所有的学生信息
    List<Student> getStudent();
    List<Student> getStudent2();
}
