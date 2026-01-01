package com.elder.care.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elder.care.dto.CreatePostDTO;
import com.elder.care.dto.CreateReplyDTO;
import com.elder.care.entity.ForumPost;
import com.elder.care.entity.ForumReply;
import com.elder.care.entity.User;
import com.elder.care.mapper.ForumPostMapper;
import com.elder.care.mapper.ForumReplyMapper;
import com.elder.care.mapper.UserMapper;
import com.elder.care.service.ForumService;
import com.elder.care.vo.ForumPostVO;
import com.elder.care.vo.ForumReplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ForumServiceImpl extends ServiceImpl<ForumPostMapper, ForumPost> implements ForumService {

    @Autowired
    private ForumReplyMapper forumReplyMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public IPage<ForumPostVO> pagePosts(Page<ForumPost> page) {
        IPage<ForumPost> postPage = baseMapper.pagePosts(page);
        Page<ForumPostVO> voPage = new Page<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
        voPage.setRecords(convertToVOList(postPage.getRecords()));
        return voPage;
    }

    @Override
    public ForumPostVO getPostDetail(Long id) {
        ForumPost post = baseMapper.getPostDetail(id);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        return convertToVO(post);
    }

    @Override
    public void createPost(Long userId, CreatePostDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        ForumPost post = new ForumPost();
        post.setUserId(userId);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setViewCount(0);
        post.setReplyCount(0);
        post.setLikeCount(0);

        baseMapper.insert(post);
    }

    @Override
    public void createReply(Long userId, CreateReplyDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        ForumPost post = baseMapper.selectById(dto.getPostId());
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }

        ForumReply reply = new ForumReply();
        reply.setPostId(dto.getPostId());
        reply.setUserId(userId);
        reply.setContent(dto.getContent());
        reply.setLikeCount(0);

        forumReplyMapper.insert(reply);

        post.setReplyCount(post.getReplyCount() + 1);
        baseMapper.updateById(post);
    }

    @Override
    public List<ForumReplyVO> listReplies(Long postId) {
        List<ForumReply> replies = forumReplyMapper.listRepliesByPostId(postId);
        return replies.stream().map(this::convertReplyToVO).collect(Collectors.toList());
    }

    @Override
    public void likePost(Long postId) {
        ForumPost post = baseMapper.selectById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        post.setLikeCount(post.getLikeCount() + 1);
        baseMapper.updateById(post);
    }

    @Override
    public void likeReply(Long replyId) {
        ForumReply reply = forumReplyMapper.selectById(replyId);
        if (reply == null) {
            throw new RuntimeException("回复不存在");
        }
        reply.setLikeCount(reply.getLikeCount() + 1);
        forumReplyMapper.updateById(reply);
    }

    private ForumPostVO convertToVO(ForumPost post) {
        ForumPostVO vo = BeanUtil.copyProperties(post, ForumPostVO.class);
        if (post.getCreateTime() != null) {
            vo.setCreateTime(post.getCreateTime());
        }
        if (post.getUpdateTime() != null) {
            vo.setUpdateTime(post.getUpdateTime());
        }
        return vo;
    }

    private List<ForumPostVO> convertToVOList(List<ForumPost> posts) {
        return posts.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    private ForumReplyVO convertReplyToVO(ForumReply reply) {
        ForumReplyVO vo = BeanUtil.copyProperties(reply, ForumReplyVO.class);
        if (reply.getCreateTime() != null) {
            vo.setCreateTime(reply.getCreateTime());
        }
        return vo;
    }
}
