package com.elder.care.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class NurseDetailVO {

    private Long id;

    private String name;

    private Integer age;

    private String avatarUrl;

    private Integer grade;

    private String gradeName;

    private BigDecimal price;

    private String skills;

    private Integer status;

    private String statusName;

    private List<EvaluationVO> evaluations;
}
