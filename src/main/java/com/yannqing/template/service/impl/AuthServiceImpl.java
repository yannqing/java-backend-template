package com.yannqing.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yannqing.template.domain.dto.auth.RegisterDto;
import com.yannqing.template.domain.entity.User;
import com.yannqing.template.enums.ErrorType;
import com.yannqing.template.enums.RoleType;
import com.yannqing.template.exception.BusinessException;
import com.yannqing.template.mapper.UserMapper;
import com.yannqing.template.service.AuthService;
import com.yannqing.template.service.RoleUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @description: 认证业务逻辑
 * @author: yannqing
 * @create: 2025-01-14 16:33
 * @from: <更多资料：yannqing.com>
 **/
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleUserService roleUserService;

    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 注册
     * @param registerDto
     */
    @Override
    public boolean register(RegisterDto registerDto) {
        String username = Optional.ofNullable(registerDto.getUsername())
                .filter(s -> !s.isEmpty())
                .orElseThrow(() -> new BusinessException(ErrorType.ARGS_NOT_NULL));
        String password = Optional.ofNullable(registerDto.getPassword())
                .filter(s -> !s.isEmpty())
                .orElseThrow(() -> new BusinessException(ErrorType.ARGS_NOT_NULL));
        String nickName = Optional.ofNullable(registerDto.getNickName())
                .filter(s -> !s.isEmpty())
                .orElseThrow(() -> new BusinessException(ErrorType.ARGS_NOT_NULL));

        // username 必须不能重复，且大于 6 个字符，小于等于 15 个字符
        if (username.length() <= 6 || username.length() > 15) {
            throw new BusinessException(ErrorType.USERNAME_LENGTH_ERROR);
        }
        boolean isUsernameExist = userMapper.exists(new QueryWrapper<User>().eq("username", username));
        if (isUsernameExist) {
            throw new BusinessException(ErrorType.USERNAME_ALREADY_EXIST);
        }

        // password 必须大于等于 8 位，小于 15 位
        if (password.length() < 8 || password.length() >= 15) {
            throw new BusinessException(ErrorType.PASSWORD_LENGTH_ERROR);
        }

        User registerUser = RegisterDto.dtoToUser(registerDto);
        registerUser.setPassword(passwordEncoder.encode(password));

        // 插入新注册用户
        int result = userMapper.insert(registerUser);
        log.info("用户user{}注册成功", registerUser.getUsername());

        // 给用户添加角色
        if (registerDto.getRoleId() == null) {
            roleUserService.addRole(username, RoleType.USER);
        } else {
            roleUserService.addRole(username, registerDto.getRoleId());
        }

        log.info("用户{}添加角色{}成功", registerUser.getUsername(), RoleType.USER.getRoleId() == registerDto.getRoleId() ? RoleType.USER.getRoleCode() : RoleType.OTHER.getRoleCode());

        return result > 0;
    }
}
