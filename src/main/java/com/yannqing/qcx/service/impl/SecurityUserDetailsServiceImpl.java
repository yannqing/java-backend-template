package com.yannqing.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yannqing.template.domain.SecurityUser;
import com.yannqing.template.domain.User;
import com.yannqing.template.mapper.UserMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SecurityUserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //获取到用户的全部信息
//        User user = userDao.getUserByUsername(username);
        log.info("loading user by username: {}", username);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        log.debug("login user: {}", user);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        return new SecurityUser(user);
    }
}
