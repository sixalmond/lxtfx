package com.elder.care.vo;

import lombok.Data;

@Data
public class UserVO {

    private Long id;

    private String realName;

    private String phone;

    private String address;

    private Integer userType;

    private String userTypeName;

    private String createTime;
}
