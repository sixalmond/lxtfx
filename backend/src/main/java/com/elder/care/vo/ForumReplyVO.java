package com.elder.care.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ForumReplyVO {

    private Long id;

    private Long postId;

    private Long userId;

    private String userName;

    private String userAvatar;

    private String content;

    private Integer likeCount;

    private LocalDateTime createTime;
}
