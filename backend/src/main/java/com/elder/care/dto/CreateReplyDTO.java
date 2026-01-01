package com.elder.care.dto;

import lombok.Data;

@Data
public class CreateReplyDTO {

    private Long postId;

    private String content;
}
