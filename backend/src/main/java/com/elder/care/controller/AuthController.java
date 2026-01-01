package com.elder.care.controller;

import com.elder.care.common.Result;
import com.elder.care.dto.LoginDTO;
import com.elder.care.dto.RegisterDTO;
import com.elder.care.entity.Nurse;
import com.elder.care.entity.User;
import com.elder.care.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {
        try {
            String token = userService.login(loginDTO);
            Object user = userService.getUserByUsername(loginDTO.getUsername(), loginDTO.getUserType());
            
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("userType", loginDTO.getUserType());
            data.put("userId", user instanceof User ? ((User) user).getId() : ((Nurse) user).getId());
            
            if (user instanceof User) {
                data.put("username", ((User) user).getRealName());
            } else {
                data.put("username", ((Nurse) user).getName());
            }
            
            return Result.success(data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterDTO registerDTO) {
        try {
            userService.register(registerDTO);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
