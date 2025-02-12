package com.yannqing.template.service;

import com.yannqing.template.domain.entity.RoleUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yannqing.template.enums.RoleType;

/**
* @author 67121
* @description 针对表【role_user】的数据库操作Service
* @createDate 2025-01-10 15:47:32
*/
public interface RoleUserService extends IService<RoleUser> {

    void addRole(String username, RoleType roleType);

    void addRole(String username, Integer roleId);

    void addRole(int userId, Integer roleId);

    void addRole(int userId, RoleType roleType);
}
