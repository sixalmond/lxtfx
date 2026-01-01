package com.elder.care.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elder.care.common.Result;
import com.elder.care.dto.CreatePostDTO;
import com.elder.care.dto.CreateReplyDTO;
import com.elder.care.service.ForumService;
import com.elder.care.vo.ForumPostVO;
import com.elder.care.vo.ForumReplyVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;

    @GetMapping("/posts")
    public Result<IPage<ForumPostVO>> pagePosts(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            IPage<ForumPostVO> page = forumService.pagePosts(new Page<>(current, size));
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/posts/{id}")
    public Result<ForumPostVO> getPostDetail(@PathVariable Long id) {
        try {
            ForumPostVO post = forumService.getPostDetail(id);
            return Result.success(post);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/posts")
    public Result<Void> createPost(
            @RequestBody CreatePostDTO dto,
            HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            forumService.createPost(userId, dto);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/replies")
    public Result<Void> createReply(
            @RequestBody CreateReplyDTO dto,
            HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            forumService.createReply(userId, dto);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/posts/{id}/replies")
    public Result<List<ForumReplyVO>> listReplies(@PathVariable Long id) {
        try {
            List<ForumReplyVO> replies = forumService.listReplies(id);
            return Result.success(replies);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/posts/{id}/like")
    public Result<Void> likePost(@PathVariable Long id) {
        try {
            forumService.likePost(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/replies/{id}/like")
    public Result<Void> likeReply(@PathVariable Long id) {
        try {
            forumService.likeReply(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
