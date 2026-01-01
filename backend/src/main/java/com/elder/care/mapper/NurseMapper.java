package com.elder.care.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elder.care.entity.Nurse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NurseMapper extends BaseMapper<Nurse> {

    @Select("SELECT * FROM nurse WHERE status = 0 ORDER BY grade DESC, create_time DESC")
    List<Nurse> listAvailableNurses();

    @Select("SELECT * FROM nurse WHERE id = #{id}")
    Nurse getNurseById(@Param("id") Long id);

    @Select("SELECT * FROM nurse WHERE status = #{status} ORDER BY create_time DESC")
    IPage<Nurse> pageByStatus(Page<Nurse> page, @Param("status") Integer status);

    @Select("SELECT * FROM nurse WHERE username = #{username}")
    Nurse findByUsername(@Param("username") String username);

    @Select("<script>" +
            "SELECT * FROM nurse " +
            "<where>" +
            "<if test='status != null'>AND status = #{status}</if>" +
            "</where>" +
            "ORDER BY create_time DESC" +
            "</script>")
    IPage<Nurse> pageNurses(Page<Nurse> page, @Param("status") Integer status);
}
