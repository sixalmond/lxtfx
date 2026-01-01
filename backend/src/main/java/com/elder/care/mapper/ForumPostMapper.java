package com.elder.care.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elder.care.entity.ForumPost;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ForumPostMapper extends BaseMapper<ForumPost> {

    @Select("SELECT p.*, u.real_name as user_name " +
            "FROM forum_post p " +
            "LEFT JOIN user u ON p.user_id = u.id " +
            "ORDER BY p.create_time DESC")
    IPage<ForumPost> pagePosts(Page<ForumPost> page);

    @Select("SELECT p.*, u.real_name as user_name " +
            "FROM forum_post p " +
            "LEFT JOIN user u ON p.user_id = u.id " +
            "WHERE p.id = #{id}")
    ForumPost getPostDetail(@Param("id") Long id);
}
