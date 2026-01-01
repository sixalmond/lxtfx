package com.elder.care.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elder.care.dto.LoginDTO;
import com.elder.care.dto.RegisterDTO;
import com.elder.care.entity.User;
import com.elder.care.vo.UserVO;

public interface UserService extends IService<User> {

    String login(LoginDTO loginDTO);

    void register(RegisterDTO registerDTO);

    User getUserById(Long id);

    Object getUserByUsername(String username, String userType);

    IPage<UserVO> pageUsers(Page<User> page);

    void updateUserType(Long userId, Integer userType);

    void deleteUser(Long userId);
}
