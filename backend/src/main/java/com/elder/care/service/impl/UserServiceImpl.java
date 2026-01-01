package com.elder.care.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elder.care.dto.LoginDTO;
import com.elder.care.dto.RegisterDTO;
import com.elder.care.entity.Nurse;
import com.elder.care.entity.SysDict;
import com.elder.care.entity.User;
import com.elder.care.mapper.NurseMapper;
import com.elder.care.mapper.UserMapper;
import com.elder.care.service.SysDictService;
import com.elder.care.service.UserService;
import com.elder.care.util.JwtUtil;
import com.elder.care.util.PasswordUtil;
import com.elder.care.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private NurseMapper nurseMapper;

    @Autowired
    private SysDictService sysDictService;

    @Override
    public String login(LoginDTO loginDTO) {
        String userType = loginDTO.getUserType();
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        Object user;

        if ("0".equals(userType)) {
            user = baseMapper.findByUsernameAndType(username, 0);
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            if (!PasswordUtil.verify(password, ((User) user).getPassword())) {
                throw new RuntimeException("Incorrect password");
            }
        } else if ("1".equals(userType)) {
            user = nurseMapper.findByUsername(username);
            if (user == null) {
                throw new RuntimeException("Nurse not found");
            }
            if (!PasswordUtil.verify(password, ((Nurse) user).getPassword())) {
                throw new RuntimeException("Incorrect password");
            }
        } else if ("2".equals(userType)) {
            user = baseMapper.findByUsernameAndType(username, 2);
            if (user == null) {
                throw new RuntimeException("Admin not found");
            }
            if (!PasswordUtil.verify(password, ((User) user).getPassword())) {
                throw new RuntimeException("Incorrect password");
            }
        } else {
            throw new RuntimeException("Invalid user type");
        }

        Long userId;
        String realName;
        
        if (user instanceof User) {
            userId = ((User) user).getId();
            realName = ((User) user).getRealName();
        } else {
            userId = ((Nurse) user).getId();
            realName = ((Nurse) user).getName();
        }

        return jwtUtil.generateToken(userId, username, userType);
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        User existUser = baseMapper.findByUsername(registerDTO.getUsername());
        if (existUser != null) {
            throw new RuntimeException("Username already exists");
        }

        User existPhone = baseMapper.findByPhone(registerDTO.getPhone());
        if (existPhone != null) {
            throw new RuntimeException("Phone number already registered");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(PasswordUtil.encrypt(registerDTO.getPassword()));
        user.setRealName(registerDTO.getRealName());
        user.setPhone(registerDTO.getPhone());
        user.setAddress(registerDTO.getAddress());
        user.setUserType(0);

        baseMapper.insert(user);
    }

    @Override
    public User getUserById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Object getUserByUsername(String username, String userType) {
        if ("0".equals(userType)) {
            return baseMapper.findByUsernameAndType(username, 0);
        } else if ("2".equals(userType)) {
            return baseMapper.findByUsernameAndType(username, 2);
        } else {
            return nurseMapper.findByUsername(username);
        }
    }

    @Override
    public IPage<UserVO> pageUsers(Page<User> page) {
        IPage<User> userPage = baseMapper.pageUsers(page);
        Page<UserVO> voPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        voPage.setRecords(convertToVOList(userPage.getRecords()));
        return voPage;
    }

    @Override
    public void updateUserType(Long userId, Integer userType) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        user.setUserType(userType);
        baseMapper.updateById(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        baseMapper.deleteById(userId);
    }

    private List<UserVO> convertToVOList(List<User> users) {
        Map<String, String> typeMap = sysDictService.listByDictType("user_type").stream()
                .collect(Collectors.toMap(d -> d.getDictCode(), SysDict::getDictName));

        return users.stream().map(user -> {
            UserVO vo = BeanUtil.copyProperties(user, UserVO.class);
            vo.setUserTypeName(typeMap.getOrDefault(String.valueOf(user.getUserType()), ""));

            if (user.getCreateTime() != null) {
                vo.setCreateTime(DateUtil.format(user.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            }

            return vo;
        }).collect(Collectors.toList());
    }
}
