package com.elder.care.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elder.care.entity.ForumReply;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ForumReplyMapper extends BaseMapper<ForumReply> {

    @Select("SELECT r.*, u.real_name as user_name " +
            "FROM forum_reply r " +
            "LEFT JOIN user u ON r.user_id = u.id " +
            "WHERE r.post_id = #{postId} " +
            "ORDER BY r.create_time ASC")
    List<ForumReply> listRepliesByPostId(@Param("postId") Long postId);
}
