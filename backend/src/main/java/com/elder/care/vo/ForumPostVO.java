package com.elder.care.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ForumPostVO {

    private Long id;

    private Long userId;

    private String userName;

    private String userAvatar;

    private String title;

    private String content;

    private Integer viewCount;

    private Integer replyCount;

    private Integer likeCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
