package com.elder.care.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderVO {

    private Long id;

    private Long userId;

    private String userName;

    private Long nurseId;

    private String nurseName;

    private String nurseAvatarUrl;

    private Integer nurseGrade;

    private BigDecimal nursePrice;

    private Integer orderStatus;

    private String orderStatusName;

    private String orderTime;

    private String confirmTime;

    private String serviceStartTime;

    private String serviceEndTime;

    private String complaintContent;

    private String handleResult;

    private Boolean evaluated;

    private EvaluationVO evaluation;
}
