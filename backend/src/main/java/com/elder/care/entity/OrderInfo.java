package com.elder.care.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("order_info")
public class OrderInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long nurseId;

    private Integer orderStatus;

    private LocalDateTime orderTime;

    private LocalDateTime confirmTime;

    private LocalDateTime serviceStartTime;

    private LocalDateTime serviceEndTime;

    private String complaintContent;

    private String handleResult;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String nurseName;

    @TableField(exist = false)
    private Integer nurseGrade;

    @TableField(exist = false)
    private java.math.BigDecimal nursePrice;

    @TableField(exist = false)
    private Boolean evaluated;
}
