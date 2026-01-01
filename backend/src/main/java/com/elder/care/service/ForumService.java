package com.elder.care.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elder.care.dto.CreatePostDTO;
import com.elder.care.dto.CreateReplyDTO;
import com.elder.care.entity.ForumPost;
import com.elder.care.vo.ForumPostVO;
import com.elder.care.vo.ForumReplyVO;

import java.util.List;

public interface ForumService extends IService<ForumPost> {

    IPage<ForumPostVO> pagePosts(Page<ForumPost> page);

    ForumPostVO getPostDetail(Long id);

    void createPost(Long userId, CreatePostDTO dto);

    void createReply(Long userId, CreateReplyDTO dto);

    List<ForumReplyVO> listReplies(Long postId);

    void likePost(Long postId);

    void likeReply(Long replyId);
}
