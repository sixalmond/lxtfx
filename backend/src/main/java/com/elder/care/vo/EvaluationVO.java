package com.elder.care.vo;

import lombok.Data;

@Data
public class EvaluationVO {

    private Long id;

    private Long orderId;

    private Integer score;

    private String evaluateContent;

    private String evaluateTime;

    private String userName;
}
